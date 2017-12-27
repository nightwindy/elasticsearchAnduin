package com.anduin.api.results;

import lombok.Data;

import java.io.Serializable;

/**
 * 车源的搜索结果类 原始对象类
 */
@Data
public class SearchB2BCarResult implements Serializable {

    private static final long serialVersionUID = 3317068920078729745L;

    private Long car_id;

    private String car_area;//销售区域

    private String car_unique;//车架号

    private String configure;//配置

    private String guide_price;//指导价格

    private String img_url;

    private String reach_date;//到港日期

    private String remark;//备注

    private String send_city;//发车城市

    private String outer_color_name;

    private String inner_color_name;

    private String model_name;

    private String displacement;//排放量

    private String drive;

    private String seat;

    private String year;

    private Long seller_id;//供应商ID

    private Long model_id;//车型ID

    private String car_status;//车辆状态

    private Long seller_price;//报价

    private Long invoice_price;//发票金额

    private Integer is_deleted;

    private Long inner_id;

    private Long outer_id;

    private Integer benchmark;//排放标准

    private Integer fuel;//燃油

    private Integer quantity;//数量

    private Integer insurance;//店内保险

    private Integer sanbao;//强制三包

    private Integer formalities_status;//手续

    private Long series_id;//车系ID

    private Long brand_id;//品牌ID

    private Integer standard;//车型表中的车规

    private Integer status;//车型状态

    private String custom_model;//自定义车型

    private String custom_formality;//自定义手续

    private String custom_area;//自定义销售区域

    private Integer spec;//车规

    private Integer car_type;//现车和期车

    private String modify;

    private Integer sku_status;//车辆上下架状态

    private String gmt_create;//创建时间

    private Integer price_adjust_way;//调价方式 0-电议 1-直接报价 2-优惠点数 3-优惠万元 4-加价

    private String price_adjust_val;//调价幅度

    private String config_price;//配置价格

    private String series_name;

    private String corp_name;

    private Integer car_source;

    private String ext_id;

    private Long creator;//发车着

    private Integer area_id; //车辆所在区ID 1东区 2南区 3西区 4北区

    private String area_name;//车辆所在区 1东区 2南区 3西区 4北区

    private Integer area_show;//车辆所在地的显示1按区域 2按省份 3按城市

    private Long activity_tag_set_id; //活动标签设置的ID

    /**
     * 是否加入买家保障计划(0.未加入 1.加入)
     */
    private Integer is_guarantee;

    /**
     * for es search begin
     * 这两个字段都是为聚合服务的
     *
     */
    private String send_city_ag;// 第一级城市

    private String spec_name;//翻译的车规

    private String wap_name;//车型的短名称

    private Float score;

}
