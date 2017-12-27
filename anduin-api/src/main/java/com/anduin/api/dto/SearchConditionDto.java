package com.anduin.api.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class SearchConditionDto {

    private String indexName;//索引名称

    private String analyzer;//分词器

    // query mode : {multi : multiMatch, term : termQuery}
    // 如果是 multi,就处理keyWord和keyWordFields
    // 如果是 term,就处理和keyWord和keyWordFields的第一条
    private String queryMode;

    private String keyWord;//搜索关键词

    private List<KeyWordFieldDto> keyWordFields;//关键词搜索的属性范围

    private List<SearchFilterDto> filters;//过滤条件

    private List<SearchMustNotDdo> mustNots;  // 过滤条件，跟fitlers相反，整个list会以and形式组合后作为过滤条件

    private List<SearchSortDto> sorts;//排序列表

    private SearchScoreSortDto searchScoreSort;//通过得分的方式排序

    private int start = 0;//起始行

    private int pageNo = 1;

    private int pageSize = 20;//查询的数量

    private Map<String, List<String>> aggs;//聚合属性和属性值列表

    private List<String> allAggFields;//所有需要聚合的属性列表

    private Class responseClazz;

    private Map<String, String> mergeRelation;

    private Float tieBreak;
}
