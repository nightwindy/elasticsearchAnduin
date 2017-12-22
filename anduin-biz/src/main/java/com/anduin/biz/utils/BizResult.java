package com.anduin.biz.utils;

import com.anduin.biz.enums.ResultCodeEnum;

import java.io.Serializable;

/**
 * Created by windy on 2017/12/22.
 */
public class BizResult<T> implements Serializable {

    private boolean success;
    private T data;
    private String errCode = "";
    private String errMsg = "";

    public BizResult() {
        this.success = true;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        this.success = false;
    }

    public void setErrMsg(ResultCodeEnum resultCodeEnum) {
        this.errMsg = resultCodeEnum.getDesc();
        this.errCode = resultCodeEnum.getCode();
        this.success = false;
    }

    public String getErrMsg(){
        return this.errMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}