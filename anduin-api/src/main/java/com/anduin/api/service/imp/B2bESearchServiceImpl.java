package com.anduin.api.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anduin.api.constant.Apiconstants;
import com.anduin.api.dto.*;
import com.anduin.api.enums.IdFilterTypeEnum;
import com.anduin.api.enums.NameFilterTypeEnum;
import com.anduin.api.enums.ScoreBuilderTypeEnum;
import com.anduin.api.enums.SearchSortTypeEnum;
import com.anduin.api.manager.EsearchManager;
import com.anduin.api.results.CarAggregationResult;
import com.anduin.api.results.GroupCarAggregationResult;
import com.anduin.api.results.SearchB2BCarResult;
import com.anduin.api.results.SearchResult;
import com.anduin.api.service.CompanyESearchService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FiltersFunctionScoreQuery;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.*;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.sort.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.*;


@Service("b2bESearchService")
public class B2bESearchServiceImpl implements CompanyESearchService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EsearchManager eSearchClientManager;


    @Override
    public SearchResult commonSearch(SearchConditionDto searchCondition) {
        //获取搜索的client
        Client client = eSearchClientManager.getClient();
        //client.prepareGet()
        final SearchRequestBuilder builder = client.prepareSearch(Apiconstants.COMPANY_INDEX_ALIAS);
        //设置搜索的起始行
        builder.setFrom(searchCondition.getStart());
        //设置搜索的分页大小
        builder.setSize(searchCondition.getPageSize());
        //是否返回权重结果的解释，为true表示返回
        builder.setExplain(true);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        //构建查询语句
        buildQuery(searchCondition, queryBuilder);

        //构建过滤条件
        buildFilters(searchCondition, queryBuilder);

        //设置查询语句
        builder.setQuery(queryBuilder);

        //设置搜索排序
        buildSort(searchCondition, builder);

        //设置得分和权重进行排序
        buildSortByScorce(searchCondition, builder, queryBuilder);
        //请求得到搜索结果
        SearchResult result = new SearchResult();
        try {
            CarAggregationResult responses = getResponse(builder);
            result.setSearchResult(responses.getSearchB2BCarResults());
            result.setSuccess(true);
            result.setTotal(responses.getTotal());
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg("搜索请求失败");
            logger.error("搜索请求失败", e);
        }
        return result;
    }

    @Override
    public SearchResult b2bCarSearch(SearchConditionDto searchCondition) {
        return null;
    }

    @Override
    public SearchResult<GroupCarAggregationResult> groupCarSearch(SearchConditionDto searchCondition) {
        return null;
    }

    @Override
    public SearchResult b2bCarBulkSearch(SearchConditionDto searchCondition) {
        return null;
    }

    @Override
    public SearchResult b2bSearch(SearchConditionDto searchCondition) {
        return null;
    }

    @Override
    public SearchResult dmsCarSourceRecommend(Long seriesId, String innerColorName, String outerColorName, String place, String areaId) {
        return null;
    }

    /**
     * 设置查询语句
     *
     * @param searchCondition 搜索条件
     * @param queryBuilder    查询builder
     */
    private void buildQuery(SearchConditionDto searchCondition, BoolQueryBuilder queryBuilder) {
        //关键词为空直接返回
        if (StringUtils.isBlank(searchCondition.getKeyWord())) {
            return;
        }
        //关键词需要匹配的字段为空也直接返回
        List<KeyWordFieldDto> keyWordFields = searchCondition.getKeyWordFields();
        if (CollectionUtils.isEmpty(searchCondition.getKeyWordFields())) {
            return;
        }
        if(StringUtils.isBlank(searchCondition.getQueryMode()) || searchCondition.getQueryMode().equals("multi")) {
            MultiMatchQueryBuilder mqb = QueryBuilders.multiMatchQuery(searchCondition.getKeyWord());
            mqb.operator(Operator.AND);
            mqb.type(MultiMatchQueryBuilder.Type.CROSS_FIELDS);
            mqb.analyzer("query_ansj");
            mqb.tieBreaker(searchCondition.getTieBreak() == null ? 0.3f : searchCondition.getTieBreak());
            //设置分词器
            if (StringUtils.isNotBlank(searchCondition.getAnalyzer())) {
                mqb.analyzer(searchCondition.getAnalyzer());
            }
            //循环遍历关键词需要匹配的所有字段
            for (KeyWordFieldDto field : keyWordFields) {
                if (StringUtils.isBlank(field.getFieldName())) {
                    continue;
                }
                //如果得分不为空的还需要设置得分
                if (field.getFieldBoost() == null) {
                    mqb.field(field.getFieldName());
                } else {
                    mqb.field(field.getFieldName(), field.getFieldBoost());
                }
            }

            queryBuilder.must(mqb);
        } else if(searchCondition.getQueryMode().equals("term")) {
            TermQueryBuilder tqb = QueryBuilders.termQuery(searchCondition.getKeyWordFields().get(0).getFieldName(),searchCondition.getKeyWord());
            queryBuilder.must(tqb);
        }

    }


    private void buildFilters(SearchConditionDto searchCondition, BoolQueryBuilder queryBuilder) {
        List<SearchFilterDto> filters = searchCondition.getFilters();
        if (!CollectionUtils.isEmpty(filters)) {
            BoolQueryBuilder innerBool = QueryBuilders.boolQuery();
            for (SearchFilterDto filter : filters) {
                //如果ID过滤条件不为空就以ID进行过滤
                if (StringUtils.isNotBlank(filter.getIdField()) && filter.getFilterId() != null) {
                    //ID的过滤类型
                    Integer idFilterType = filter.getIdFiltertype();
                    //默认是等于
                    if (idFilterType == null || idFilterType.equals(IdFilterTypeEnum.eq.getType())) {
                        innerBool.must(QueryBuilders.termQuery(filter.getIdField(), filter.getFilterId()));
                    } else if (idFilterType.equals(IdFilterTypeEnum.rt.getType())) {
                        //大于
                        innerBool.must(QueryBuilders.rangeQuery(filter.getIdField()).gt(filter.getFilterId()));
                    } else if (idFilterType.equals(IdFilterTypeEnum.lt.getType())) {
                        //小于
                        innerBool.must(QueryBuilders.rangeQuery(filter.getIdField()).lt(filter.getFilterId()));
                    } else if (idFilterType.equals(IdFilterTypeEnum.ne.getType())) {
                        innerBool.mustNot(QueryBuilders.termQuery(filter.getIdField(), filter.getFilterId()));
                    }
                } else if (StringUtils.isNotBlank(filter.getNameField()) && StringUtils.isNotBlank(filter.getFilterName())) {
                    //使用名称进行过滤
                    //名称的过滤类型
                    Integer nameFilterType = filter.getNameFiltertype();
                    //默认是完全匹配
                    if (nameFilterType == null || NameFilterTypeEnum.exact.getType().equals(nameFilterType)) {
                        innerBool.must(QueryBuilders.termQuery(filter.getNameField(), filter.getFilterName()));
                    } else if (NameFilterTypeEnum.fuzzy.getType().equals(nameFilterType)) {
                        //模糊匹配
                        innerBool.must(QueryBuilders.fuzzyQuery(filter.getNameField(), filter.getFilterName()));
                    }
                } else {
                    /*//过滤掉个人非实名认证的
                    if (filter.getIdFiltertype().equals(IdFilterTypeEnum.empty.getType())){
                        BoolQueryBuilder baseFilterBool = merchantAndPersonalVerifiedFilter();
                        innerBool.must(baseFilterBool);
                    }*/
                }
            }
            // 保留满足条件的内容
            queryBuilder.filter(innerBool);
        }

        List<SearchMustNotDdo> mustNots = searchCondition.getMustNots();
        if (!CollectionUtils.isEmpty(mustNots)) {
            BoolQueryBuilder innerBool = QueryBuilders.boolQuery();
            for (SearchMustNotDdo filter : mustNots) {
                //如果ID过滤条件不为空就以ID进行过滤
                if (StringUtils.isNotBlank(filter.getIdField()) && filter.getFilterId() != null) {
                    //ID的过滤类型
                    Integer idFilterType = filter.getIdFiltertype();
                    //默认是等于
                    if (idFilterType == null || idFilterType.equals(IdFilterTypeEnum.eq.getType())) {
                        innerBool.must(QueryBuilders.termQuery(filter.getIdField(), filter.getFilterId()));
                    } else if (idFilterType.equals(IdFilterTypeEnum.rt.getType())) {
                        //大于
                        innerBool.must(QueryBuilders.rangeQuery(filter.getIdField()).gt(filter.getFilterId()));
                    } else if (idFilterType.equals(IdFilterTypeEnum.lt.getType())) {
                        //小于
                        innerBool.must(QueryBuilders.rangeQuery(filter.getIdField()).lt(filter.getFilterId()));
                    }
                } else if (StringUtils.isNotBlank(filter.getNameField()) && StringUtils.isNotBlank(filter.getFilterName())) {
                    //使用名称进行过滤
                    //名称的过滤类型
                    Integer nameFilterType = filter.getNameFiltertype();
                    //默认是完全匹配
                    if (nameFilterType == null || NameFilterTypeEnum.exact.getType().equals(nameFilterType)) {
                        innerBool.must(QueryBuilders.termQuery(filter.getNameField(), filter.getFilterName()));
                    } else if (NameFilterTypeEnum.fuzzy.getType().equals(nameFilterType)) {
                        //模糊匹配
                        innerBool.must(QueryBuilders.fuzzyQuery(filter.getNameField(), filter.getFilterName()));
                    }
                }
            }
            // 过滤
            queryBuilder.mustNot(innerBool);
        }
    }

    /**
     * 构建排序条件
     *
     * @param searchCondition 搜索条件
     * @param builder         搜索builder
     */
    private void buildSort(SearchConditionDto searchCondition, SearchRequestBuilder builder) {
        List<SearchSortDto> sorts = searchCondition.getSorts();
        if (CollectionUtils.isEmpty(sorts)) {
            return;
        }
        for (SearchSortDto sort : sorts) {
            SortBuilder sortBuilder;
            if(StringUtils.isNotEmpty(sort.getType())  && "geo".equals(sort.getType())) {
                sortBuilder = new GeoDistanceSortBuilder(sort.getSortWord(), Double.class.cast(sort.getExt().get("lat")), Double.class.cast(sort.getExt().get("lon")))
                        .unit(DistanceUnit.KILOMETERS)
                        .geoDistance(GeoDistance.PLANE);
            } else {
                sortBuilder = new FieldSortBuilder(sort.getSortWord());
            }
            Integer sortType = sort.getSortType();
            //设置降序还是升序
            if (sortType == null || SearchSortTypeEnum.asc.getType().equals(sortType)) {
                sortBuilder.order(SortOrder.ASC);
            } else if (SearchSortTypeEnum.des.getType().equals(sortType)) {
                sortBuilder.order(SortOrder.DESC);
            }
            builder.addSort(sortBuilder);
        }

    }

    /**
     * 返回搜素结果
     *
     * @param builder
     * @return
     */
    private CarAggregationResult getResponse(SearchRequestBuilder builder) {
        // get response
        SearchResponse response = builder.get();
        List<SearchB2BCarResult> beanList = null;
        CarAggregationResult result = new CarAggregationResult();
        if (response != null && Apiconstants.OK.equals(Integer.valueOf(response.status().getStatus()))) {
            if (response.getHits() != null && response.getHits().getHits() != null && response.getHits().hits().length > 0) {
                beanList = new LinkedList<SearchB2BCarResult>();
                SearchHits hits = response.getHits();
                for (SearchHit tmp : hits.hits()) {
                    SearchB2BCarResult res = JSON.parseObject(tmp.sourceAsString(), SearchB2BCarResult.class);
                    beanList.add(res);
                }
                result.setSearchB2BCarResults(beanList);
                result.setTotal((int)response.getHits().getTotalHits());
            }
        } else {
            throw new RuntimeException("搜索请求失败!");
        }

        return result;
    }

    /**
     * 通过设置得分方式排序
     * function\_score会在查询结束后对每一个匹配的文档进行一系列的重打分操作，最后以生成的最终分数进行排序
     * @param searchCondition 搜索条件
     * @param builder         搜索builder
     */
    private void buildSortByScorce(SearchConditionDto searchCondition, SearchRequestBuilder builder, BoolQueryBuilder queryBuilder) {
        SearchScoreSortDto searchScoreSort = searchCondition.getSearchScoreSort();
        if (searchScoreSort == null) {
            return;
        }
        List<ScoreSortDto> scoreSorts = searchCondition.getSearchScoreSort().getScoreSorts();
        if (CollectionUtils.isEmpty(scoreSorts)) {
            return;
        }
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] functions = new FunctionScoreQueryBuilder.FilterFunctionBuilder[scoreSorts.size()];

        for (int i=0; i < scoreSorts.size(); i++ ) {
            ScoreSortDto scoreSort = scoreSorts.get(i);
            Integer scoreType = scoreSort.getScoreBuilderType();
            if (ScoreBuilderTypeEnum.fieldValue.getType().equals(scoreType)) {
                FieldValueFactorFunctionBuilder fielScore = ScoreFunctionBuilders.fieldValueFactorFunction(scoreSort.getField());
                //设置因子系数
                if (scoreSort.getFactor() != null) {
                    fielScore.factor(scoreSort.getFactor());
                }
                //设置为空时的默认值
                if (scoreSort.getDefaultValue() != null) {
                    fielScore.missing(scoreSort.getDefaultValue());
                }
                //设置权重
                if (scoreSort.getWeight() != null) {
                    fielScore.setWeight(scoreSort.getWeight());
                }
                functions[i] = new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        fielScore);
            } else if (ScoreBuilderTypeEnum.linearDecay.getType().equals(scoreType)) {
                LinearDecayFunctionBuilder linearScore;
                if(scoreSort.getDecay() == null && scoreSort.getOffset() == null ) {
                    linearScore = ScoreFunctionBuilders.linearDecayFunction(scoreSort.getField(), scoreSort.getOrigin() == null ? 0 : scoreSort.getOrigin(), scoreSort.getScale());
                } else if (scoreSort.getDecay() == null) {
                    linearScore = ScoreFunctionBuilders.linearDecayFunction(scoreSort.getField(), scoreSort.getOrigin() == null ? 0 : scoreSort.getOrigin(), scoreSort.getScale(), scoreSort.getOffset());
                } else {
                    linearScore = ScoreFunctionBuilders.linearDecayFunction(scoreSort.getField(), scoreSort.getOrigin() == null ? 0 : scoreSort.getOrigin(), scoreSort.getScale(), scoreSort.getOffset(), scoreSort.getDecay());
                }
                //设置权重
                if (scoreSort.getWeight() != null) {
                    linearScore.setWeight(scoreSort.getWeight());
                }
                functions[i] = new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        linearScore);
            } else if (ScoreBuilderTypeEnum.random.getType().equals(scoreType)) {

                // 确保同一会话的随机分一致
                RandomScoreFunctionBuilder randScore = randomFunction(scoreSort.getSeed());
                //设置权重
                if (scoreSort.getWeight() != null) {
                    randScore.setWeight(scoreSort.getWeight());
                }
                functions[i] = new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        randScore);
            } else if (ScoreBuilderTypeEnum.script.getType().equals(scoreType)) {
                //换位值了
                //public Script(String script, ScriptType type, String lang, @Nullable Map<String, ?> params) 5.02
                //Script(ScriptType type, String lang, String idOrCode, Map<String, Object> params) 5.6.3
                Script script = new Script( ScriptType.INLINE,scoreSort.getScript(), "groovy",new HashedMap());
                ScriptScoreFunctionBuilder scriptScore = ScoreFunctionBuilders.scriptFunction(script);
                if (scoreSort.getWeight() != null) {
                    scriptScore.setWeight(scoreSort.getWeight());
                }
                functions[i] = new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        scriptScore);
            }
        }
        FunctionScoreQueryBuilder fsqb = QueryBuilders.functionScoreQuery(queryBuilder, functions);
        /**
         * Score mode defines how the combined result of score functions will influence the final score together with the sub query score.
         * Can be replace, avg, max, sum, min, multiply
         */
        if (StringUtils.isNotBlank(searchScoreSort.getBoostMode())) {
            fsqb.boostMode(CombineFunction.fromString(searchScoreSort.getBoostMode()));
        }
        /**
         * Score mode defines how results of individual score functions will be aggregated.
         * Can be first, avg, max, sum, min, multiply
         */
        if (StringUtils.isNotBlank(searchScoreSort.getScoreMode())) {
            fsqb.scoreMode(FiltersFunctionScoreQuery.ScoreMode.fromString(searchScoreSort.getScoreMode()));
        }
        builder.setQuery(fsqb);

    }
}
