package com.beman.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录VO
 */
@Data
public class TransactionVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 交易类型：1-收入，2-支出
     */
    private Integer type;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类图标
     */
    private String categoryIcon;

    /**
     * 分类颜色
     */
    private String categoryColor;

    /**
     * 描述
     */
    private String description;

    /**
     * 交易日期
     */
    private LocalDateTime transactionDate;

    /**
     * 支付方式：1-现金，2-支付宝，3-微信，4-银行卡
     */
    private Integer paymentMethod;

    /**
     * 支付方式名称
     */
    private String paymentMethodName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态：0-待确认，1-已确认，2-已取消
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
