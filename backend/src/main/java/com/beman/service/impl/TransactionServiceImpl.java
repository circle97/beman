package com.beman.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.mapper.CategoryMapper;
import com.beman.mapper.TransactionMapper;
import com.beman.model.Category;
import com.beman.model.Transaction;
import com.beman.model.dto.TransactionCreateDTO;
import com.beman.model.dto.TransactionQueryDTO;
import com.beman.model.vo.FinanceStatsVO;
import com.beman.model.vo.TransactionVO;
import com.beman.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 交易记录服务实现类
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public Transaction createTransaction(TransactionCreateDTO createDTO) {
        Long userId = StpUtil.getLoginIdAsLong();

        // 检查分类
        Category category = categoryMapper.selectById(createDTO.getCategoryId());
        if (category == null || (category.getUserId() != 0 && !Objects.equals(category.getUserId(), userId))) {
            throw new RuntimeException("分类不存在或无权限");
        }

        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setType(createDTO.getType());
        transaction.setAmount(createDTO.getAmount());
        transaction.setCategoryId(createDTO.getCategoryId());
        transaction.setCategoryName(category.getName());
        transaction.setCategoryIcon(category.getIcon());
        transaction.setDescription(createDTO.getDescription());
        transaction.setTransactionDate(createDTO.getTransactionDate() != null ? createDTO.getTransactionDate() : LocalDateTime.now());
        transaction.setPaymentMethod(createDTO.getPaymentMethod() == null ? 1 : createDTO.getPaymentMethod());
        transaction.setRemark(createDTO.getRemark());
        transaction.setStatus(1);

        transactionMapper.insert(transaction);
        return transaction;
    }

    @Override
    public IPage<TransactionVO> getTransactionPage(TransactionQueryDTO queryDTO) {
        Long userId = StpUtil.getLoginIdAsLong();

        Page<Transaction> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        QueryWrapper<Transaction> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (queryDTO.getType() != null) {
            wrapper.eq("type", queryDTO.getType());
        }
        if (queryDTO.getCategoryId() != null) {
            wrapper.eq("category_id", queryDTO.getCategoryId());
        }
        if (queryDTO.getStartDate() != null) {
            wrapper.ge("transaction_date", queryDTO.getStartDate().atStartOfDay());
        }
        if (queryDTO.getEndDate() != null) {
            wrapper.le("transaction_date", queryDTO.getEndDate().atTime(LocalTime.MAX));
        }
        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.like("description", queryDTO.getKeyword());
        }
        wrapper.orderByDesc("transaction_date");

        IPage<Transaction> entityPage = transactionMapper.selectPage(page, wrapper);

        // 转换为 VO 分页
        Page<TransactionVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        List<TransactionVO> voRecords = entityPage.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        voPage.setRecords(voRecords);
        return voPage;
    }

    @Override
    public TransactionVO getTransactionDetail(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Transaction transaction = transactionMapper.selectById(id);
        if (transaction == null || !Objects.equals(transaction.getUserId(), userId)) {
            throw new RuntimeException("交易记录不存在");
        }
        return toVO(transaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Transaction transaction = transactionMapper.selectById(id);
        if (transaction == null || !Objects.equals(transaction.getUserId(), userId)) {
            throw new RuntimeException("交易记录不存在或无权限");
        }
        transactionMapper.deleteById(id);
    }

    @Override
    public FinanceStatsVO getFinanceStats() {
        Long userId = StpUtil.getLoginIdAsLong();
        LocalDate today = LocalDate.now();

        // 月度区间
        LocalDateTime monthStart = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime monthEnd = today.withDayOfMonth(today.lengthOfMonth()).atTime(LocalTime.MAX);

        // 年度区间
        LocalDateTime yearStart = today.withDayOfYear(1).atStartOfDay();
        LocalDateTime yearEnd = today.withDayOfYear(today.lengthOfYear()).atTime(LocalTime.MAX);

        BigDecimal monthIncome = sumAmount(userId, 1, monthStart, monthEnd);
        BigDecimal monthExpense = sumAmount(userId, 2, monthStart, monthEnd);
        BigDecimal yearIncome = sumAmount(userId, 1, yearStart, yearEnd);
        BigDecimal yearExpense = sumAmount(userId, 2, yearStart, yearEnd);

        FinanceStatsVO vo = new FinanceStatsVO();
        vo.setMonthIncome(nvl(monthIncome));
        vo.setMonthExpense(nvl(monthExpense));
        vo.setMonthBalance(vo.getMonthIncome().subtract(vo.getMonthExpense()));
        vo.setYearIncome(nvl(yearIncome));
        vo.setYearExpense(nvl(yearExpense));
        vo.setYearBalance(vo.getYearIncome().subtract(vo.getYearExpense()));
        // 其他看板数据后续补充，先返回空/0，满足前端主流程
        vo.setTotalAssets(BigDecimal.ZERO);
        vo.setTotalLiabilities(BigDecimal.ZERO);
        vo.setNetWorth(BigDecimal.ZERO);
        vo.setExpenseCategoryStats(Collections.emptyList());
        vo.setIncomeCategoryStats(Collections.emptyList());
        vo.setWeeklyExpenseTrend(Collections.emptyList());
        vo.setWeeklyIncomeTrend(Collections.emptyList());
        vo.setPaymentMethodStats(Collections.emptyList());
        return vo;
    }

    private BigDecimal sumAmount(Long userId, int type, LocalDateTime start, LocalDateTime end) {
        QueryWrapper<Transaction> wrapper = new QueryWrapper<>();
        wrapper.select("IFNULL(SUM(amount),0) AS amount");
        wrapper.eq("user_id", userId)
               .eq("type", type)
               .between("transaction_date", start, end);
        // 这里用自定义聚合较麻烦，改为查询列表后累加以保证兼容性
        wrapper.clear();
        wrapper.eq("user_id", userId)
               .eq("type", type)
               .between("transaction_date", start, end);
        List<Transaction> list = transactionMapper.selectList(wrapper);
        return list.stream().map(Transaction::getAmount).filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private TransactionVO toVO(Transaction t) {
        TransactionVO vo = new TransactionVO();
        vo.setId(t.getId());
        vo.setType(t.getType());
        vo.setAmount(t.getAmount());
        vo.setCategoryId(t.getCategoryId());
        vo.setCategoryName(t.getCategoryName());
        vo.setCategoryIcon(t.getCategoryIcon());
        // categoryColor 可在后续扩展时补充
        vo.setDescription(t.getDescription());
        vo.setTransactionDate(t.getTransactionDate());
        vo.setPaymentMethod(t.getPaymentMethod());
        vo.setPaymentMethodName(paymentMethodName(t.getPaymentMethod()));
        vo.setRemark(t.getRemark());
        vo.setStatus(t.getStatus());
        vo.setCreateTime(t.getCreateTime());
        return vo;
    }

    private String paymentMethodName(Integer method) {
        if (method == null) return "现金";
        switch (method) {
            case 1: return "现金";
            case 2: return "支付宝";
            case 3: return "微信";
            case 4: return "银行卡";
            default: return "其他";
        }
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}


