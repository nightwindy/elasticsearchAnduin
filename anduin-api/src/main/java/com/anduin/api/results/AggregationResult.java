package com.anduin.api.results;

import lombok.Data;


@Data
public class AggregationResult {

    private Long id;//属性的ID

    private String name;//属性名称

    private long num;//聚合的个数

    private String year;// 年款

    private String specName; // 车规名

    private String guidePrice; // 指导价

    public AggregationResult() {
    }

    public AggregationResult(Long id, String name, long num){
        this.id = id;
        this.name = name;
        this.num = num;
    }

    public AggregationResult(Long id, String name, long num, String year, String guidePrice){
        this.id = id;
        this.name = name;
        this.num = num;
        this.year = year;
        this.guidePrice = guidePrice;
    }
}
