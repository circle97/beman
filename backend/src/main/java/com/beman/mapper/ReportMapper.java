package com.beman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.model.Report;
import com.beman.model.vo.ReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 举报Mapper接口
 */
@Mapper
public interface ReportMapper extends BaseMapper<Report> {

    /**
     * 分页查询举报列表
     */
    IPage<ReportVO> selectReportPage(Page<ReportVO> page, @Param("status") Integer status, 
                                    @Param("contentType") Integer contentType, @Param("priority") Integer priority,
                                    @Param("handlerId") Long handlerId, @Param("keyword") String keyword);

    /**
     * 查询举报详情
     */
    ReportVO selectReportDetail(@Param("id") Long id);

    /**
     * 查询用户的举报历史
     */
    List<ReportVO> selectUserReports(@Param("reporterId") Long reporterId);

    /**
     * 查询内容的举报数量
     */
    Integer selectContentReportCount(@Param("contentType") Integer contentType, @Param("contentId") Long contentId);

    /**
     * 更新举报状态
     */
    int updateReportStatus(@Param("id") Long id, @Param("status") Integer status, 
                          @Param("result") String result, @Param("handlerId") Long handlerId);

    /**
     * 查询待处理的举报数量
     */
    Integer selectPendingReportCount();

    /**
     * 查询高优先级举报数量
     */
    Integer selectHighPriorityReportCount();
}
