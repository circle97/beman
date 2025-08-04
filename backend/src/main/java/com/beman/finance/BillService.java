package com.beman.finance;

import com.beman.finance.dto.BillRequest;
import com.beman.finance.dto.BillResponse;
import com.beman.finance.dto.CategorySummary;
import com.beman.finance.dto.FinanceSummary;
import com.beman.finance.entity.Bill;
import com.beman.finance.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;

    @Transactional
    public BillResponse addBill(Long userId, BillRequest request) {
        Bill bill = new Bill();
        bill.setUserId(userId);
        bill.setAmount(request.getAmount());
        bill.setType(request.getType());
        bill.setDate(request.getDate());
        bill.setCategory(request.getCategory());
        bill.setDescription(request.getDescription());
        bill.setShared(request.isShared());
        bill.setSharedPercentage(request.getSharedPercentage());

        Bill savedBill = billRepository.save(bill);
        return convertToBillResponse(savedBill);
    }

    public List<BillResponse> getBills(Long userId, Bill.BillType type, String category,
                                     LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Bill> bills;

        if (type != null && category != null) {
            bills = billRepository.findByUserIdAndTypeAndCategoryAndDateBetween(
                    userId, type, category, startDate, endDate, pageable);
        } else if (type != null) {
            bills = billRepository.findByUserIdAndTypeAndDateBetween(
                    userId, type, startDate, endDate, pageable);
        } else if (category != null) {
            bills = billRepository.findByUserIdAndCategoryAndDateBetween(
                    userId, category, startDate, endDate, pageable);
        } else {
            bills = billRepository.findByUserIdAndDateBetween(
                    userId, startDate, endDate, pageable);
        }

        return bills.getContent().stream()
                .map(this::convertToBillResponse)
                .collect(Collectors.toList());
    }

    public FinanceSummary getFinanceSummary(Long userId, String period) {
        LocalDate startDate;
        LocalDate endDate = LocalDate.now();

        switch (period.toLowerCase()) {
            case "week":
                startDate = endDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                break;
            case "month":
                startDate = endDate.withDayOfMonth(1);
                break;
            case "year":
                startDate = endDate.withDayOfYear(1);
                break;
            default:
                startDate = endDate.withDayOfMonth(1);
        }

        BigDecimal totalIncome = billRepository.sumAmountByUserIdAndTypeAndDateBetween(
                userId, Bill.BillType.INCOME, startDate, endDate);
        BigDecimal totalExpense = billRepository.sumAmountByUserIdAndTypeAndDateBetween(
                userId, Bill.BillType.EXPENSE, startDate, endDate);

        if (totalIncome == null) totalIncome = BigDecimal.ZERO;
        if (totalExpense == null) totalExpense = BigDecimal.ZERO;

        BigDecimal balance = totalIncome.subtract(totalExpense);

        List<CategorySummary> categorySummaries = getCategorySummaries(userId, startDate, endDate);

        return FinanceSummary.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .categorySummaries(categorySummaries)
                .period(period)
                .build();
    }

    private List<CategorySummary> getCategorySummaries(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Object[]> expenseSummaries = billRepository.getCategorySummary(
                userId, Bill.BillType.EXPENSE, startDate, endDate);
        List<Object[]> incomeSummaries = billRepository.getCategorySummary(
                userId, Bill.BillType.INCOME, startDate, endDate);

        List<CategorySummary> summaries = expenseSummaries.stream()
                .map(row -> CategorySummary.builder()
                        .category((String) row[0])
                        .amount((BigDecimal) row[1])
                        .count((Long) row[2])
                        .build())
                .collect(Collectors.toList());

        summaries.addAll(incomeSummaries.stream()
                .map(row -> CategorySummary.builder()
                        .category((String) row[0])
                        .amount((BigDecimal) row[1])
                        .count((Long) row[2])
                        .build())
                .collect(Collectors.toList()));

        return summaries;
    }

    private BillResponse convertToBillResponse(Bill bill) {
        return BillResponse.builder()
                .id(bill.getId())
                .userId(bill.getUserId())
                .amount(bill.getAmount())
                .type(bill.getType())
                .date(bill.getDate())
                .category(bill.getCategory())
                .description(bill.getDescription())
                .isShared(bill.isShared())
                .sharedPercentage(bill.getSharedPercentage())
                .createdAt(bill.getCreatedAt())
                .updatedAt(bill.getUpdatedAt())
                .build();
    }
} 