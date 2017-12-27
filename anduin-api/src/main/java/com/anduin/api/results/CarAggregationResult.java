package com.anduin.api.results;

import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class CarAggregationResult {

    //搜索结果信息
    private List<SearchB2BCarResult> searchB2BCarResults;

    //筛选结果信息
    Map<String, List<AggregationResult>> aggregationMap;

    private int total;//搜索结果总数

    private int pageNo;

    private String errMsg;//错误信息
}
