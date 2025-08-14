package com.beman.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 财务统计VO
 */
@Data
public class FinanceStatsVO {

    /**
     * 本月收入
     */
    private BigDecimal monthIncome;

    /**
     * 本月支出
     */
    private BigDecimal monthExpense;

    /**
     * 本月结余
     */
    private BigDecimal monthBalance;

    /**
     * 本年收入
     */
    private BigDecimal yearIncome;

    /**
     * 本年支出
     */
    private BigDecimal yearExpense;

    /**
     * 本年结余
     */
    private BigDecimal yearBalance;

    /**
     * 总资产
     */
    private BigDecimal totalAssets;

    /**
     * 总负债
     */
    private BigDecimal totalLiabilities;

    /**
     * 净资产
     */
    private BigDecimal netWorth;

    /**
     * 本月支出分类统计
     */
    private List<CategoryStatsVO> expenseCategoryStats;

    /**
     * 本月收入分类统计
     */
    private List<CategoryStatsVO> incomeCategoryStats;

    /**
     * 最近7天支出趋势
     */
    private List<DailyStatsVO> weeklyExpenseTrend;

    /**
     * 最近7天收入趋势
     */
    private List<DailyStatsVO> weeklyIncomeTrend;

    /**
     * 支付方式统计
     */
    private List<PaymentMethodStatsVO> paymentMethodStats;

    /**
     * 数据类
     */
    @Data
    public static class CategoryStatsVO {
        private Long categoryId;
        private String categoryName;
        private String categoryIcon;
        private String categoryColor;
        private BigDecimal amount;
        private BigDecimal percentage;
    }

    @Data
    public static class DailyStatsVO {
        private String date;
        private BigDecimal amount;
    }

    @Data
    public static class PaymentMethodStatsVO {
        private Integer paymentMethod;
        private String paymentMethodName;
        private BigDecimal amount;
        private BigDecimal percentage;
    }
}
