package com.beman.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 礼物推荐查询DTO
 */
@Data
public class GiftRecommendationQueryDTO {

    /**
     * 分页参数
     */
    private Integer page = 1;
    private Integer size = 20;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 价格范围
     */
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    /**
     * 年龄范围
     */
    private Integer minAge;
    private Integer maxAge;

    /**
     * 适用性别：0-不限，1-男，2-女
     */
    private Integer gender;

    /**
     * 适用场合
     */
    private List<String> occasions;

    /**
     * 礼物类型
     */
    private List<String> categories;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 推荐指数范围
     */
    private Integer minRating;
    private Integer maxRating;

    /**
     * 排序字段：rating-推荐指数，price-价格，createTime-创建时间
     */
    private String sortBy = "rating";

    /**
     * 排序方向：asc-升序，desc-降序
     */
    private String sortOrder = "desc";

    /**
     * 是否只显示启用的推荐
     */
    private Boolean onlyEnabled = true;
}
