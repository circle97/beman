package com.beman.finance.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class FinanceSummary {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
    private List<CategorySummary> categorySummaries;
    private String period;
} 