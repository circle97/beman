package com.beman.finance;

import com.beman.common.ApiResponse;
import com.beman.finance.dto.BillRequest;
import com.beman.finance.dto.BillResponse;
import com.beman.finance.dto.FinanceSummary;
import com.beman.finance.entity.Bill;
import com.beman.security.CurrentUser;
import com.beman.user.dto.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/finance/bills")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    @PostMapping
    public ApiResponse<BillResponse> addBill(
            @CurrentUser UserDetailsImpl currentUser,
            @Valid @RequestBody BillRequest request) {
        return ApiResponse.success(billService.addBill(currentUser.getId(), request));
    }

    @GetMapping
    public ApiResponse<List<BillResponse>> getBills(
            @CurrentUser UserDetailsImpl currentUser,
            @RequestParam(required = false) Bill.BillType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(billService.getBills(
                currentUser.getId(), type, category, startDate, endDate, page, size));
    }

    @GetMapping("/summary")
    public ApiResponse<FinanceSummary> getFinanceSummary(
            @CurrentUser UserDetailsImpl currentUser,
            @RequestParam String period) {
        return ApiResponse.success(billService.getFinanceSummary(currentUser.getId(), period));
    }
}