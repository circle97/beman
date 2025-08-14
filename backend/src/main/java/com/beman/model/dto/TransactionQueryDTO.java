package com.beman.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 交易记录查询DTO
 */
@Data
public class TransactionQueryDTO {

    /**
     * 页码
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer size = 20;

    /**
     * 交易类型：1-收入，2-支出
     */
    private Integer type;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 支付方式：1-现金，2-支付宝，3-微信，4-银行卡
     */
    private Integer paymentMethod;

    /**
     * 最小金额
     */
    private String minAmount;

    /**
     * 最大金额
     */
    private String maxAmount;
}
