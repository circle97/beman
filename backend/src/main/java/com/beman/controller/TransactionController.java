package com.beman.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beman.model.Transaction;
import com.beman.model.dto.TransactionCreateDTO;
import com.beman.model.dto.TransactionQueryDTO;
import com.beman.model.vo.FinanceStatsVO;
import com.beman.model.vo.Result;
import com.beman.model.vo.TransactionVO;
import com.beman.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 交易记录控制器
 */
@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * 创建交易记录
     */
    @PostMapping
    public Result<Transaction> createTransaction(@Valid @RequestBody TransactionCreateDTO createDTO) {
        try {
            Transaction transaction = transactionService.createTransaction(createDTO);
            return Result.success("记录成功", transaction);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询交易记录
     */
    @GetMapping("/page")
    public Result<IPage<TransactionVO>> getTransactionPage(TransactionQueryDTO queryDTO) {
        try {
            IPage<TransactionVO> page = transactionService.getTransactionPage(queryDTO);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取交易记录详情
     */
    @GetMapping("/{id}")
    public Result<TransactionVO> getTransactionDetail(@PathVariable Long id) {
        try {
            TransactionVO transaction = transactionService.getTransactionDetail(id);
            return Result.success(transaction);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除交易记录
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取财务统计
     */
    @GetMapping("/stats")
    public Result<FinanceStatsVO> getFinanceStats() {
        try {
            FinanceStatsVO stats = transactionService.getFinanceStats();
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
