package com.anduin.api.dto;

import lombok.Data;

import java.util.Map;


@Data
public class SearchSortDto {

    private String type;

    private Map<String, Double> ext;

    private String sortWord;//排序的字段

    private Integer sortType;//排序方式 升序 降序

    private Float weight;//权重
}
