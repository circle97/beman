package com.beman.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beman.model.Report;
import com.beman.model.dto.ReportDTO;
import com.beman.model.dto.ReportQueryDTO;
import com.beman.model.vo.Result;
import com.beman.model.vo.ReportVO;
import com.beman.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 举报控制器
 */
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Validated
public class ReportController {

    private final ReportService reportService;

    /**
     * 创建举报
     */
    @PostMapping
    public Result<Report> createReport(@Valid @RequestBody ReportDTO reportDTO) {
        try {
            Report report = reportService.createReport(reportDTO);
            return Result.success("举报提交成功", report);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询举报列表
     */
    @GetMapping("/page")
    public Result<IPage<ReportVO>> getReportPage(ReportQueryDTO queryDTO) {
        try {
            IPage<ReportVO> result = reportService.getReportPage(queryDTO);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取举报详情
     */
    @GetMapping("/{id}")
    public Result<ReportVO> getReportDetail(@PathVariable Long id) {
        try {
            ReportVO report = reportService.getReportDetail(id);
            return Result.success(report);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 处理举报
     */
    @PutMapping("/{id}/handle")
    public Result<String> handleReport(@PathVariable Long id, 
                                     @RequestParam String result, 
                                     @RequestParam Integer status) {
        try {
            reportService.handleReport(id, result, status);
            return Result.success("举报处理成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 驳回举报
     */
    @PutMapping("/{id}/reject")
    public Result<String> rejectReport(@PathVariable Long id, @RequestParam String reason) {
        try {
            reportService.rejectReport(id, reason);
            return Result.success("举报已驳回");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 关闭举报
     */
    @PutMapping("/{id}/close")
    public Result<String> closeReport(@PathVariable Long id, @RequestParam String reason) {
        try {
            reportService.closeReport(id, reason);
            return Result.success("举报已关闭");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户的举报历史
     */
    @GetMapping("/user/{userId}")
    public Result<List<ReportVO>> getUserReports(@PathVariable Long userId) {
        try {
            List<ReportVO> reports = reportService.getUserReports(userId);
            return Result.success(reports);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取内容的举报数量
     */
    @GetMapping("/count/{contentType}/{contentId}")
    public Result<Integer> getContentReportCount(@PathVariable Integer contentType, 
                                               @PathVariable Long contentId) {
        try {
            Integer count = reportService.getContentReportCount(contentType, contentId);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取待处理举报数量
     */
    @GetMapping("/count/pending")
    public Result<Integer> getPendingReportCount() {
        try {
            Integer count = reportService.getPendingReportCount();
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取高优先级举报数量
     */
    @GetMapping("/count/high-priority")
    public Result<Integer> getHighPriorityReportCount() {
        try {
            Integer count = reportService.getHighPriorityReportCount();
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量处理举报
     */
    @PutMapping("/batch-handle")
    public Result<String> batchHandleReports(@RequestParam List<Long> reportIds, 
                                           @RequestParam String result, 
                                           @RequestParam Integer status) {
        try {
            reportService.batchHandleReports(reportIds, result, status);
            return Result.success("批量处理成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
