package com.beman.model.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.util.List;

/**
 * 礼物推荐创建DTO
 */
@Data
public class GiftRecommendationCreateDTO {

    @NotBlank(message = "礼物名称不能为空")
    private String name;

    @NotBlank(message = "礼物描述不能为空")
    private String description;

    private String imageUrl;

    @NotNull(message = "最低价格不能为空")
    @Min(value = 0, message = "最低价格不能小于0")
    private BigDecimal minPrice;

    @NotNull(message = "最高价格不能为空")
    @Min(value = 0, message = "最高价格不能小于0")
    private BigDecimal maxPrice;

    @Min(value = 0, message = "最低年龄不能小于0")
    private Integer minAge;

    @Max(value = 120, message = "最高年龄不能大于120")
    private Integer maxAge;

    @NotNull(message = "适用性别不能为空")
    @Min(value = 0, message = "适用性别值无效")
    @Max(value = 2, message = "适用性别值无效")
    private Integer gender;

    @NotNull(message = "适用场合不能为空")
    private List<String> occasions;

    @NotNull(message = "礼物类型不能为空")
    private List<String> categories;

    private List<String> tags;

    @NotNull(message = "推荐指数不能为空")
    @Min(value = 1, message = "推荐指数不能小于1")
    @Max(value = 5, message = "推荐指数不能大于5")
    private Integer rating;

    @NotBlank(message = "推荐理由不能为空")
    private String reason;

    private String purchaseUrl;
}
