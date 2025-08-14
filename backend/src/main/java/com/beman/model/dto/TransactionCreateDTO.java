package com.beman.model.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录创建DTO
 */
@Data
public class TransactionCreateDTO {

    /**
     * 交易类型：1-收入，2-支出
     */
    @NotNull(message = "交易类型不能为空")
    private Integer type;

    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    private BigDecimal amount;

    /**
     * 分类ID
     */
    @NotNull(message = "分类不能为空")
    private Long categoryId;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    private String description;

    /**
     * 交易日期
     */
    private LocalDateTime transactionDate;

    /**
     * 支付方式：1-现金，2-支付宝，3-微信，4-银行卡
     */
    private Integer paymentMethod = 1;

    /**
     * 备注
     */
    private String remark;
}
