package com.anduin.api.dto;

import lombok.Data;

import java.util.List;


@Data
public class SearchScoreSortDto {

    /**
     * 需要计算得分的列表
     */
    List<ScoreSortDto> scoreSorts;

    /**
     * Score mode defines how the combined result of score functions will influence the final score together with the sub query score.
     * Can be replace, avg, max, sum, min, multiply
     */
    private String boostMode;

    /**
     * Score mode defines how results of individual score functions will be aggregated.
     * Can be first, avg, max, sum, min, multiply
     */
    private String scoreMode;

}
