package com.beman.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 礼物推荐实体类
 */
@Data
@TableName("gift_recommendation")
public class GiftRecommendation {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 礼物名称
     */
    private String name;

    /**
     * 礼物描述
     */
    private String description;

    /**
     * 礼物图片URL
     */
    private String imageUrl;

    /**
     * 价格范围：最低价
     */
    private BigDecimal minPrice;

    /**
     * 价格范围：最高价
     */
    private BigDecimal maxPrice;

    /**
     * 适用年龄：最低年龄
     */
    private Integer minAge;

    /**
     * 适用年龄：最高年龄
     */
    private Integer maxAge;

    /**
     * 适用性别：0-不限，1-男，2-女
     */
    private Integer gender;

    /**
     * 适用场合：JSON格式，如["生日", "结婚", "节日"]
     */
    private String occasions;

    /**
     * 礼物类型：JSON格式，如["电子产品", "服装", "玩具"]
     */
    private String categories;

    /**
     * 标签：JSON格式，用于智能匹配
     */
    private String tags;

    /**
     * 推荐指数：1-5星
     */
    private Integer rating;

    /**
     * 推荐理由
     */
    private String reason;

    /**
     * 购买链接
     */
    private String purchaseUrl;

    /**
     * 状态：1-启用，2-禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer deleted;
}
