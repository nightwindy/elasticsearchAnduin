package com.anduin.biz.enums;


public enum SmsOperatorsEnum {

    LSM("LUO_SI_MAO", "螺丝帽"),
    YUN_PIAN("YUN_PIAN", "云片");

    private final String code;
    private final String desc;

    SmsOperatorsEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String code() {
        return code;
    }

    public String desc() {
        return desc;
    }
}
