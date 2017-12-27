package com.anduin.api.results;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 原始对象类
 */
@Data
public class SearchGroupCarResult implements Serializable {
    private Long goup_buy_product_id;
    private Long car_id;
    private Long model_id;
    private String model_name;
    private Long brand_id;
    private Integer spec;
    private String spec_name;
    private String car_unique;
    private Integer shopping_num;
    private Integer sale_num;
    private Integer total;
    private String send_city;
    private Integer area_id;
    private String take_car_time;
    private String out_color_name;
    private String inner_color_name;
    private String guide_price;
    private Integer adjust_way;
    private String adjust_value;
    private Long earnest;
    private Long sell_price;
    private Integer is_deleted;
    private Date start_time;
    private Date end_time;
    private Date gmt_create;
    private Date gmt_modified;
    private Integer sort;
    private String mobile;
    private String top_pics;
    private String desc_pics;
    private String tag_pic;
    private String encode_id;
    private Integer product_type;
    private Long warehouse_id;
    private String warehouse_name;
    private String extra_info;
    private String promotion_price;
    private Integer is_crowd_fund;
    private String corp_name;
    private Integer source;
    private Integer is_show_real_corp_name;
    private String brand_name;
    private String brand_letter;
    private String brand_logo;
    private String series_name;
    private Long series_id;
    private Long city_id;
    private Integer car_is_deleted;
    private Integer sku_status;
    private Long warehouse_province_id;
}
