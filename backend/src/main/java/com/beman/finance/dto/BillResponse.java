package com.beman.finance.dto;

import com.beman.finance.entity.Bill;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class BillResponse {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private Bill.BillType type;
    private LocalDate date;
    private String category;
    private String description;
    private boolean isShared;
    private Integer sharedPercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 