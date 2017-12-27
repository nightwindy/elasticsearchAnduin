package com.anduin.api.service;


import com.anduin.api.dto.SearchConditionDto;
import com.anduin.api.results.GroupCarAggregationResult;
import com.anduin.api.results.SearchResult;

public interface CompanyESearchService {

    /**
     * 通用的搜索接口
     * @param searchCondition 综合的搜索条件
     * @return 返回搜索结果
     */
    SearchResult commonSearch(SearchConditionDto searchCondition);

    /**
     * 车源的搜索接口
     * @param searchCondition 综合的搜索条件
     * @return 返回搜索结果
     */
    SearchResult b2bCarSearch(SearchConditionDto searchCondition);

    /**
     * 秒车库车源的搜索接口
     * @param searchCondition 综合的搜索条件
     * @return 返回搜索结果
     */
    SearchResult<GroupCarAggregationResult> groupCarSearch(SearchConditionDto searchCondition);

    /**
     * 车源的搜索接口
     * @param searchCondition 综合的搜索条件
     * @return 返回搜索结果
     */
    SearchResult b2bCarBulkSearch(SearchConditionDto searchCondition);

    /**
     * 通用搜索接口
     * @param searchCondition 综合的搜索条件
     * @return 返回搜索结果
     */
    SearchResult b2bSearch(SearchConditionDto searchCondition);

    /**
     * dms 车源推荐
     *
     * @param seriesId
     * @param innerColorName
     * @param outerColorName
     * @param place
     * @return
     */
    SearchResult dmsCarSourceRecommend(Long seriesId, String innerColorName, String outerColorName, String place, String areaId);
}
