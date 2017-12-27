package com.anduin.api.dto;

import lombok.Data;


@Data
public class ScoreSortDto {

    /**
     * 负载因子
     */
    private Float factor;

    /**
     * 缺失时的默认值
     */
    private Double defaultValue;

    /**
     * 起始值
     */
    private Object offset;

    /**
     * 权重
     */
    private Float weight;

    /**
     * 需要设置得分的属性名
     */
    private String field;

    /**
     * 衰减速率
     */
    private Double decay;

    /**
     * score builder type
     */
    private Integer scoreBuilderType;

    private Object origin;

    private Object scale;

    // script score query
    private String script;

    // 随机函数的 seed
    private String seed;
}
