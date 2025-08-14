package com.beman.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beman.model.Transaction;
import com.beman.model.dto.TransactionCreateDTO;
import com.beman.model.dto.TransactionQueryDTO;
import com.beman.model.vo.FinanceStatsVO;
import com.beman.model.vo.TransactionVO;

/**
 * 交易记录Service
 */
public interface TransactionService {

    /**
     * 创建交易记录
     */
    Transaction createTransaction(TransactionCreateDTO createDTO);

    /**
     * 分页查询交易记录
     */
    IPage<TransactionVO> getTransactionPage(TransactionQueryDTO queryDTO);

    /**
     * 获取交易记录详情
     */
    TransactionVO getTransactionDetail(Long id);

    /**
     * 删除交易记录
     */
    void deleteTransaction(Long id);

    /**
     * 获取财务统计
     */
    FinanceStatsVO getFinanceStats();
}
