package com.anduin.api.results;

import lombok.Data;

import java.io.Serializable;

/**
 * 搜索结果类
 * @param <T>
 */
@Data
public class SearchResult<T> implements Serializable {
    private static final long serialVersionUID = 5753503589675894819L;

    /**
     * 接口是否调用成功
     */
    private boolean success;

    /**
     * 搜索错误编码
     */
    private String errorCode;

    /**
     * 搜索错误消息
     */
    private String errorMsg;

    /**
     * 搜索结果
     */
    private T searchResult;

    /**
     * 查询结果的总数
     */
    private int total;

    private int pageNo;

}
