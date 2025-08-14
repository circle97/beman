package com.beman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.model.Transaction;
import com.beman.model.vo.TransactionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 交易记录Mapper
 */
@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {

    /**
     * 分页查询交易记录
     */
    IPage<TransactionVO> selectTransactionPage(Page<TransactionVO> page, @Param("userId") Long userId,
                                              @Param("type") Integer type, @Param("categoryId") Long categoryId,
                                              @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                              @Param("keyword") String keyword, @Param("paymentMethod") Integer paymentMethod,
                                              @Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);

    /**
     * 获取本月收入
     */
    BigDecimal selectMonthIncome(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);

    /**
     * 获取本月支出
     */
    BigDecimal selectMonthExpense(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);

    /**
     * 获取本年收入
     */
    BigDecimal selectYearIncome(@Param("userId") Long userId, @Param("year") int year);

    /**
     * 获取本年支出
     */
    BigDecimal selectYearExpense(@Param("userId") Long userId, @Param("year") int year);

    /**
     * 获取支出分类统计
     */
    List<Object> selectExpenseCategoryStats(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);

    /**
     * 获取收入分类统计
     */
    List<Object> selectIncomeCategoryStats(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);

    /**
     * 获取最近7天支出趋势
     */
    List<Object> selectWeeklyExpenseTrend(@Param("userId") Long userId);

    /**
     * 获取最近7天收入趋势
     */
    List<Object> selectWeeklyIncomeTrend(@Param("userId") Long userId);

    /**
     * 获取支付方式统计
     */
    List<Object> selectPaymentMethodStats(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);
}
