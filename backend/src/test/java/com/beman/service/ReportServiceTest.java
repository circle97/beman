package com.beman.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.mapper.ReportMapper;
import com.beman.mapper.UserMapper;
import com.beman.model.Report;
import com.beman.model.User;
import com.beman.model.dto.ReportDTO;
import com.beman.model.dto.ReportQueryDTO;
import com.beman.model.vo.ReportVO;
import com.beman.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportMapper reportMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ReportServiceImpl reportService;

    private User testUser;
    private Report testReport;
    private ReportDTO testReportDTO;
    private ReportQueryDTO testReportQueryDTO;

    @BeforeEach
    void setUp() {
        // 设置测试数据
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setNickname("测试用户");

        testReport = new Report();
        testReport.setId(1L);
        testReport.setReporterId(1L);
        testReport.setContentType(1);
        testReport.setContentId(1L);
        testReport.setContentTitle("测试内容");
        testReport.setReason("测试举报原因");
        testReport.setPriority(1);
        testReport.setStatus(0);
        testReport.setCreateTime(LocalDateTime.now());
        testReport.setUpdateTime(LocalDateTime.now());

        testReportDTO = new ReportDTO();
        testReportDTO.setContentType(1);
        testReportDTO.setContentId(1L);
        testReportDTO.setContentTitle("测试内容");
        testReportDTO.setReason("测试举报原因");
        testReportDTO.setPriority(1);

        testReportQueryDTO = new ReportQueryDTO();
        testReportQueryDTO.setPage(1);
        testReportQueryDTO.setSize(10);
    }

    @Test
    void testCreateReport_Success() {
        // 准备测试数据
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(reportMapper.insert(any(Report.class))).thenReturn(1);

        // 执行测试
        Report result = reportService.createReport(testReportDTO, 1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(testReportDTO.getContentType(), result.getContentType());
        assertEquals(testReportDTO.getContentId(), result.getContentId());
        assertEquals(testReportDTO.getContentTitle(), result.getContentTitle());
        assertEquals(testReportDTO.getReason(), result.getReason());
        assertEquals(testReportDTO.getPriority(), result.getPriority());
        assertEquals(1L, result.getReporterId());
        assertEquals(0, result.getStatus());
        assertNotNull(result.getCreateTime());
        assertNotNull(result.getUpdateTime());

        // 验证方法调用
        verify(userMapper).selectById(1L);
        verify(reportMapper).insert(any(Report.class));
    }

    @Test
    void testCreateReport_UserNotFound() {
        // 准备测试数据
        when(userMapper.selectById(1L)).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            reportService.createReport(testReportDTO, 1L);
        });

        // 验证方法调用
        verify(userMapper).selectById(1L);
        verify(reportMapper, never()).insert(any(Report.class));
    }

    @Test
    void testGetReportPage_Success() {
        // 准备测试数据
        List<Report> reports = Arrays.asList(testReport);
        Page<Report> page = new Page<>(1, 10);
        page.setRecords(reports);
        page.setTotal(1);

        when(reportMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(page);
        when(userMapper.selectById(1L)).thenReturn(testUser);

        // 执行测试
        IPage<ReportVO> result = reportService.getReportPage(testReportQueryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        assertEquals(testReport.getContentTitle(), result.getRecords().get(0).getContentTitle());
        assertEquals(testUser.getNickname(), result.getRecords().get(0).getReporterName());

        // 验证方法调用
        verify(reportMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
        verify(userMapper).selectById(1L);
    }

    @Test
    void testGetReportPage_EmptyResult() {
        // 准备测试数据
        Page<Report> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList());
        page.setTotal(0);

        when(reportMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(page);

        // 执行测试
        IPage<ReportVO> result = reportService.getReportPage(testReportQueryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertEquals(0, result.getRecords().size());

        // 验证方法调用
        verify(reportMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
        verify(userMapper, never()).selectById(anyLong());
    }

    @Test
    void testGetReportById_Success() {
        // 准备测试数据
        when(reportMapper.selectById(1L)).thenReturn(testReport);
        when(userMapper.selectById(1L)).thenReturn(testUser);

        // 执行测试
        Optional<ReportVO> result = reportService.getReportById(1L);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals(testReport.getContentTitle(), result.get().getContentTitle());
        assertEquals(testUser.getNickname(), result.get().getReporterName());

        // 验证方法调用
        verify(reportMapper).selectById(1L);
        verify(userMapper).selectById(1L);
    }

    @Test
    void testGetReportById_NotFound() {
        // 准备测试数据
        when(reportMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        Optional<ReportVO> result = reportService.getReportById(1L);

        // 验证结果
        assertFalse(result.isPresent());

        // 验证方法调用
        verify(reportMapper).selectById(1L);
        verify(userMapper, never()).selectById(anyLong());
    }

    @Test
    void testHandleReport_Success() {
        // 准备测试数据
        when(reportMapper.selectById(1L)).thenReturn(testReport);
        when(reportMapper.updateById(any(Report.class))).thenReturn(1);

        // 执行测试
        boolean result = reportService.handleReport(1L, "已处理", 1L);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(reportMapper).selectById(1L);
        verify(reportMapper).updateById(any(Report.class));
    }

    @Test
    void testHandleReport_NotFound() {
        // 准备测试数据
        when(reportMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        boolean result = reportService.handleReport(1L, "已处理", 1L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(reportMapper).selectById(1L);
        verify(reportMapper, never()).updateById(any(Report.class));
    }

    @Test
    void testRejectReport_Success() {
        // 准备测试数据
        when(reportMapper.selectById(1L)).thenReturn(testReport);
        when(reportMapper.updateById(any(Report.class))).thenReturn(1);

        // 执行测试
        boolean result = reportService.rejectReport(1L, "举报无效", 1L);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(reportMapper).selectById(1L);
        verify(reportMapper).updateById(any(Report.class));
    }

    @Test
    void testRejectReport_NotFound() {
        // 准备测试数据
        when(reportMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        boolean result = reportService.rejectReport(1L, "举报无效", 1L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(reportMapper).selectById(1L);
        verify(reportMapper, never()).updateById(any(Report.class));
    }

    @Test
    void testGetReportCount_Success() {
        // 准备测试数据
        when(reportMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(5L);

        // 执行测试
        int result = reportService.getReportCount(0);

        // 验证结果
        assertEquals(5, result);

        // 验证方法调用
        verify(reportMapper).selectCount(any(LambdaQueryWrapper.class));
    }

    @Test
    void testGetReportCount_Zero() {
        // 准备测试数据
        when(reportMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

        // 执行测试
        int result = reportService.getReportCount(0);

        // 验证结果
        assertEquals(0, result);

        // 验证方法调用
        verify(reportMapper).selectCount(any(LambdaQueryWrapper.class));
    }

    @Test
    void testGetReportsByContent_Success() {
        // 准备测试数据
        List<Report> reports = Arrays.asList(testReport);
        when(reportMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(reports);
        when(userMapper.selectById(1L)).thenReturn(testUser);

        // 执行测试
        List<ReportVO> result = reportService.getReportsByContent(1, 1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testReport.getContentTitle(), result.get(0).getContentTitle());
        assertEquals(testUser.getNickname(), result.get(0).getReporterName());

        // 验证方法调用
        verify(reportMapper).selectList(any(LambdaQueryWrapper.class));
        verify(userMapper).selectById(1L);
    }

    @Test
    void testGetReportsByContent_Empty() {
        // 准备测试数据
        when(reportMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Arrays.asList());

        // 执行测试
        List<ReportVO> result = reportService.getReportsByContent(1, 1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.size());

        // 验证方法调用
        verify(reportMapper).selectList(any(LambdaQueryWrapper.class));
        verify(userMapper, never()).selectById(anyLong());
    }

    @Test
    void testBatchHandleReports_Success() {
        // 准备测试数据
        List<Long> reportIds = Arrays.asList(1L, 2L);
        when(reportMapper.selectById(1L)).thenReturn(testReport);
        when(reportMapper.selectById(2L)).thenReturn(testReport);
        when(reportMapper.updateById(any(Report.class))).thenReturn(1);

        // 执行测试
        int result = reportService.batchHandleReports(reportIds, "批量处理", 1L);

        // 验证结果
        assertEquals(2, result);

        // 验证方法调用
        verify(reportMapper, times(2)).selectById(anyLong());
        verify(reportMapper, times(2)).updateById(any(Report.class));
    }

    @Test
    void testBatchHandleReports_PartialSuccess() {
        // 准备测试数据
        List<Long> reportIds = Arrays.asList(1L, 2L);
        when(reportMapper.selectById(1L)).thenReturn(testReport);
        when(reportMapper.selectById(2L)).thenReturn(null);
        when(reportMapper.updateById(any(Report.class))).thenReturn(1);

        // 执行测试
        int result = reportService.batchHandleReports(reportIds, "批量处理", 1L);

        // 验证结果
        assertEquals(1, result);

        // 验证方法调用
        verify(reportMapper, times(2)).selectById(anyLong());
        verify(reportMapper, times(1)).updateById(any(Report.class));
    }

    @Test
    void testGetReportStatistics_Success() {
        // 准备测试数据
        when(reportMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(10L, 5L, 3L, 2L);

        // 执行测试
        var result = reportService.getReportStatistics();

        // 验证结果
        assertNotNull(result);
        assertEquals(10, result.get("total"));
        assertEquals(5, result.get("pending"));
        assertEquals(3, result.get("handled"));
        assertEquals(2, result.get("rejected"));

        // 验证方法调用
        verify(reportMapper, times(4)).selectCount(any(LambdaQueryWrapper.class));
    }
}
