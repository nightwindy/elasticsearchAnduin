package com.anduin.api.enums;

import lombok.Getter;


public enum NameFilterTypeEnum {

    exact(1,"完全匹配"),
    fuzzy(2,"模糊匹配"),
    ;

    @Getter
    private Integer type;

    @Getter
    private String desc;

    NameFilterTypeEnum(Integer type, String desc){
        this.type = type;
        this.desc = desc;
    }
}
