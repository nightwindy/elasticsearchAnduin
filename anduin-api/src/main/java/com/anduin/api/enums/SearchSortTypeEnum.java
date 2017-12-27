package com.anduin.api.enums;

import lombok.Getter;


public enum SearchSortTypeEnum {

    asc(1,"升序"),
    des(2,"降序"),
    ;

    @Getter
    private Integer type;

    @Getter
    private String desc;

    SearchSortTypeEnum(Integer type, String desc){
        this.type = type;
        this.desc = desc;
    }


}
