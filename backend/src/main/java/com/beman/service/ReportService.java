package com.beman.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beman.model.Report;
import com.beman.model.dto.ReportDTO;
import com.beman.model.dto.ReportQueryDTO;
import com.beman.model.vo.ReportVO;

import java.util.List;

/**
 * 举报服务接口
 */
public interface ReportService {

    /**
     * 创建举报
     */
    Report createReport(ReportDTO reportDTO);

    /**
     * 分页查询举报列表
     */
    IPage<ReportVO> getReportPage(ReportQueryDTO queryDTO);

    /**
     * 获取举报详情
     */
    ReportVO getReportDetail(Long id);

    /**
     * 处理举报
     */
    void handleReport(Long id, String result, Integer status);

    /**
     * 驳回举报
     */
    void rejectReport(Long id, String reason);

    /**
     * 关闭举报
     */
    void closeReport(Long id, String reason);

    /**
     * 获取用户的举报历史
     */
    List<ReportVO> getUserReports(Long userId);

    /**
     * 获取内容的举报数量
     */
    Integer getContentReportCount(Integer contentType, Long contentId);

    /**
     * 获取待处理举报数量
     */
    Integer getPendingReportCount();

    /**
     * 获取高优先级举报数量
     */
    Integer getHighPriorityReportCount();

    /**
     * 批量处理举报
     */
    void batchHandleReports(List<Long> reportIds, String result, Integer status);
}
