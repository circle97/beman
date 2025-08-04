package com.beman.finance.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class CategorySummary {
    private String category;
    private BigDecimal amount;
    private Long count;
} 