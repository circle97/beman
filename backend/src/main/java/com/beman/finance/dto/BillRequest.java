package com.beman.finance.dto;

import com.beman.finance.entity.Bill;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BillRequest {
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    private BigDecimal amount;

    @NotNull(message = "账单类型不能为空")
    private Bill.BillType type;

    @NotNull(message = "日期不能为空")
    private LocalDate date;

    @NotNull(message = "分类不能为空")
    private String category;

    private String description;

    private boolean isShared = false;

    private Integer sharedPercentage = 50;
} 