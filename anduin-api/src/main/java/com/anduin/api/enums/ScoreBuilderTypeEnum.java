package com.anduin.api.enums;

import lombok.Getter;

/**
 * 衰减函数还可以指定三种不同的模式：线性函数（linear）、以 e 为底的指数函数（Exp）和高斯函数（gauss），它们拥有不同的衰减曲线：
 */
public enum ScoreBuilderTypeEnum {

    fieldValue(1,"FieldValueFactorFunctionBuilder"),
    linearDecay(2,"LinearDecayFunctionBuilder"),
    random(3,"RandomScoreFunctionBuilder"),
    script(4,"ScriptScoreFunctionBuilder"),
    ;

    @Getter
    private Integer type;

    @Getter
    private String desc;

    ScoreBuilderTypeEnum(Integer type, String desc){
        this.type = type;
        this.desc = desc;
    }
}
