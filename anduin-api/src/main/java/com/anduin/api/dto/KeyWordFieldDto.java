package com.anduin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyWordFieldDto {

    private String fieldName;//关键字搜索的属性

    private Float fieldBoost;//对应属性的占比得分
}
