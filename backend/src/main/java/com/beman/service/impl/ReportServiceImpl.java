package com.beman.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.mapper.ReportMapper;
import com.beman.model.Report;
import com.beman.model.User;
import com.beman.model.dto.ReportDTO;
import com.beman.model.dto.ReportQueryDTO;
import com.beman.model.vo.ReportVO;
import com.beman.service.ReportService;
import com.beman.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 举报服务实现类
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;
    private final UserService userService;

    @Override
    @Transactional
    public Report createReport(ReportDTO reportDTO) {
        // 获取当前用户信息
        Long reporterId = StpUtil.getLoginIdAsLong();
        User reporter = userService.findById(reporterId);
        
        // 检查是否重复举报
        // TODO: 实现重复举报检查逻辑
        
        // 创建举报记录
        Report report = new Report();
        report.setReporterId(reporterId);
        report.setReporterName(reporter.getUsername());
        report.setContentType(reportDTO.getContentType());
        report.setContentId(reportDTO.getContentId());
        report.setReasonType(reportDTO.getReasonType());
        report.setDescription(reportDTO.getDescription());
        report.setEvidence(reportDTO.getEvidence());
        report.setStatus(0); // 待处理
        report.setPriority(calculatePriority(reportDTO.getReasonType())); // 根据原因类型计算优先级
        report.setCreateTime(LocalDateTime.now());
        report.setUpdateTime(LocalDateTime.now());
        
        reportMapper.insert(report);
        return report;
    }

    @Override
    public IPage<ReportVO> getReportPage(ReportQueryDTO queryDTO) {
        Page<ReportVO> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        
        return reportMapper.selectReportPage(
            page, queryDTO.getStatus(), queryDTO.getContentType(), 
            queryDTO.getPriority(), queryDTO.getHandlerId(), queryDTO.getKeyword()
        );
    }

    @Override
    public ReportVO getReportDetail(Long id) {
        return reportMapper.selectReportDetail(id);
    }

    @Override
    @Transactional
    public void handleReport(Long id, String result, Integer status) {
        Long handlerId = StpUtil.getLoginIdAsLong();
        User handler = userService.findById(handlerId);
        
        int updated = reportMapper.updateReportStatus(id, status, result, handlerId);
        if (updated == 0) {
            throw new RuntimeException("举报不存在或状态更新失败");
        }
        
        // 如果举报被处理，可能需要执行相应的操作
        // 比如删除违规内容、警告用户等
        if (status == 2) { // 已处理
            // TODO: 实现相应的处理逻辑
        }
    }

    @Override
    @Transactional
    public void rejectReport(Long id, String reason) {
        Long handlerId = StpUtil.getLoginIdAsLong();
        
        int updated = reportMapper.updateReportStatus(id, 3, reason, handlerId); // 3-已驳回
        if (updated == 0) {
            throw new RuntimeException("举报不存在或状态更新失败");
        }
    }

    @Override
    @Transactional
    public void closeReport(Long id, String reason) {
        Long handlerId = StpUtil.getLoginIdAsLong();
        
        int updated = reportMapper.updateReportStatus(id, 4, reason, handlerId); // 4-已关闭
        if (updated == 0) {
            throw new RuntimeException("举报不存在或状态更新失败");
        }
    }

    @Override
    public List<ReportVO> getUserReports(Long userId) {
        return reportMapper.selectUserReports(userId);
    }

    @Override
    public Integer getContentReportCount(Integer contentType, Long contentId) {
        return reportMapper.selectContentReportCount(contentType, contentId);
    }

    @Override
    public Integer getPendingReportCount() {
        return reportMapper.selectPendingReportCount();
    }

    @Override
    public Integer getHighPriorityReportCount() {
        return reportMapper.selectHighPriorityReportCount();
    }

    @Override
    @Transactional
    public void batchHandleReports(List<Long> reportIds, String result, Integer status) {
        Long handlerId = StpUtil.getLoginIdAsLong();
        
        for (Long reportId : reportIds) {
            reportMapper.updateReportStatus(reportId, status, result, handlerId);
        }
    }

    /**
     * 根据举报原因类型计算优先级
     */
    private Integer calculatePriority(Integer reasonType) {
        switch (reasonType) {
            case 1: // 垃圾信息
                return 2; // 中优先级
            case 2: // 不当内容
                return 3; // 高优先级
            case 3: // 恶意行为
                return 4; // 紧急
            case 4: // 版权问题
                return 3; // 高优先级
            case 5: // 其他
                return 1; // 低优先级
            default:
                return 1; // 默认低优先级
        }
    }
}
