package com.anduin.api.enums;

import lombok.Getter;


public enum IdFilterTypeEnum {

    eq(1,"等于"),
    rt(2,"大于"),
    lt(3,"小于"),
    empty(4,"为空"),
    ne(5,"不等于")
    ;

    @Getter
    private Integer type;

    @Getter
    private String desc;

    IdFilterTypeEnum(Integer type, String desc){
        this.type = type;
        this.desc = desc;
    }
}
