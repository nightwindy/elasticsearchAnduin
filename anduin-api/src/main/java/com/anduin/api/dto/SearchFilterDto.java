package com.anduin.api.dto;

import lombok.Data;


@Data
public class SearchFilterDto {

    private String idField;//id对应的属性  字段  字段名称 name

    private String nameField;//name对应的属性

    private Object filterId;//过滤条件id   字段对应的单个值 张三

    private String filterName;//过滤条件的name

    private Integer idFiltertype;//id过滤类型 null或1等于 2大于 3小于

    private Integer nameFiltertype;//name过滤类型
}
