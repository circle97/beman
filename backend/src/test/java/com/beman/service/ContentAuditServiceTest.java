package com.beman.service;

import com.beman.mapper.ContentAuditMapper;
import com.beman.model.AuditResult;
import com.beman.model.dto.ContentAuditDTO;
import com.beman.service.impl.ContentAuditServiceImpl;
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
class ContentAuditServiceTest {

    @Mock
    private ContentAuditMapper contentAuditMapper;

    @InjectMocks
    private ContentAuditServiceImpl contentAuditService;

    private ContentAuditDTO testContentAuditDTO;
    private AuditResult testAuditResult;

    @BeforeEach
    void setUp() {
        // 设置测试数据
        testContentAuditDTO = new ContentAuditDTO();
        testContentAuditDTO.setContent("测试内容");
        testContentAuditDTO.setContentType(1);
        testContentAuditDTO.setUserId(1L);

        testAuditResult = new AuditResult();
        testAuditResult.setId(1L);
        testAuditResult.setContent("测试内容");
        testAuditResult.setContentType(1);
        testAuditResult.setUserId(1L);
        testAuditResult.setIsExtreme(false);
        testAuditResult.setRiskLevel(0);
        testAuditResult.setSuggestion("内容正常");
        testAuditResult.setCreateTime(LocalDateTime.now());
    }

    @Test
    void testAuditContent_Success() {
        // 准备测试数据
        when(contentAuditMapper.insert(any(AuditResult.class))).thenReturn(1);

        // 执行测试
        AuditResult result = contentAuditService.auditContent(testContentAuditDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(testContentAuditDTO.getContent(), result.getContent());
        assertEquals(testContentAuditDTO.getContentType(), result.getContentType());
        assertEquals(testContentAuditDTO.getUserId(), result.getUserId());
        assertNotNull(result.getCreateTime());

        // 验证方法调用
        verify(contentAuditMapper).insert(any(AuditResult.class));
    }

    @Test
    void testAuditContent_ExtremeContent() {
        // 准备测试数据
        testContentAuditDTO.setContent("极端内容");
        when(contentAuditMapper.insert(any(AuditResult.class))).thenReturn(1);

        // 执行测试
        AuditResult result = contentAuditService.auditContent(testContentAuditDTO);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.getIsExtreme());
        assertTrue(result.getRiskLevel() > 0);
        assertNotNull(result.getSuggestion());

        // 验证方法调用
        verify(contentAuditMapper).insert(any(AuditResult.class));
    }

    @Test
    void testAuditContent_EmptyContent() {
        // 准备测试数据
        testContentAuditDTO.setContent("");

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            contentAuditService.auditContent(testContentAuditDTO);
        });

        // 验证方法调用
        verify(contentAuditMapper, never()).insert(any(AuditResult.class));
    }

    @Test
    void testAuditContent_NullContent() {
        // 准备测试数据
        testContentAuditDTO.setContent(null);

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            contentAuditService.auditContent(testContentAuditDTO);
        });

        // 验证方法调用
        verify(contentAuditMapper, never()).insert(any(AuditResult.class));
    }

    @Test
    void testGetAuditResult_Success() {
        // 准备测试数据
        when(contentAuditMapper.selectById(1L)).thenReturn(testAuditResult);

        // 执行测试
        Optional<AuditResult> result = contentAuditService.getAuditResult(1L);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals(testAuditResult.getContent(), result.get().getContent());
        assertEquals(testAuditResult.getIsExtreme(), result.get().getIsExtreme());

        // 验证方法调用
        verify(contentAuditMapper).selectById(1L);
    }

    @Test
    void testGetAuditResult_NotFound() {
        // 准备测试数据
        when(contentAuditMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        Optional<AuditResult> result = contentAuditService.getAuditResult(1L);

        // 验证结果
        assertFalse(result.isPresent());

        // 验证方法调用
        verify(contentAuditMapper).selectById(1L);
    }

    @Test
    void testGetAuditResultsByUser_Success() {
        // 准备测试数据
        List<AuditResult> auditResults = Arrays.asList(testAuditResult);
        when(contentAuditMapper.selectList(any())).thenReturn(auditResults);

        // 执行测试
        List<AuditResult> result = contentAuditService.getAuditResultsByUser(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testAuditResult.getContent(), result.get(0).getContent());

        // 验证方法调用
        verify(contentAuditMapper).selectList(any());
    }

    @Test
    void testGetAuditResultsByUser_Empty() {
        // 准备测试数据
        when(contentAuditMapper.selectList(any())).thenReturn(Arrays.asList());

        // 执行测试
        List<AuditResult> result = contentAuditService.getAuditResultsByUser(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.size());

        // 验证方法调用
        verify(contentAuditMapper).selectList(any());
    }

    @Test
    void testGetAuditResultsByContentType_Success() {
        // 准备测试数据
        List<AuditResult> auditResults = Arrays.asList(testAuditResult);
        when(contentAuditMapper.selectList(any())).thenReturn(auditResults);

        // 执行测试
        List<AuditResult> result = contentAuditService.getAuditResultsByContentType(1);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testAuditResult.getContentType(), result.get(0).getContentType());

        // 验证方法调用
        verify(contentAuditMapper).selectList(any());
    }

    @Test
    void testGetAuditResultsByContentType_Empty() {
        // 准备测试数据
        when(contentAuditMapper.selectList(any())).thenReturn(Arrays.asList());

        // 执行测试
        List<AuditResult> result = contentAuditService.getAuditResultsByContentType(1);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.size());

        // 验证方法调用
        verify(contentAuditMapper).selectList(any());
    }

    @Test
    void testGetAuditStatistics_Success() {
        // 准备测试数据
        when(contentAuditMapper.selectCount(any())).thenReturn(10L, 5L, 3L, 2L);

        // 执行测试
        var result = contentAuditService.getAuditStatistics();

        // 验证结果
        assertNotNull(result);
        assertEquals(10, result.get("total"));
        assertEquals(5, result.get("normal"));
        assertEquals(3, result.get("warning"));
        assertEquals(2, result.get("extreme"));

        // 验证方法调用
        verify(contentAuditMapper, times(4)).selectCount(any());
    }

    @Test
    void testUpdateAuditResult_Success() {
        // 准备测试数据
        when(contentAuditMapper.selectById(1L)).thenReturn(testAuditResult);
        when(contentAuditMapper.updateById(any(AuditResult.class))).thenReturn(1);

        // 执行测试
        boolean result = contentAuditService.updateAuditResult(1L, "更新后的建议", 1);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(contentAuditMapper).selectById(1L);
        verify(contentAuditMapper).updateById(any(AuditResult.class));
    }

    @Test
    void testUpdateAuditResult_NotFound() {
        // 准备测试数据
        when(contentAuditMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        boolean result = contentAuditService.updateAuditResult(1L, "更新后的建议", 1);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(contentAuditMapper).selectById(1L);
        verify(contentAuditMapper, never()).updateById(any(AuditResult.class));
    }

    @Test
    void testDeleteAuditResult_Success() {
        // 准备测试数据
        when(contentAuditMapper.selectById(1L)).thenReturn(testAuditResult);
        when(contentAuditMapper.deleteById(1L)).thenReturn(1);

        // 执行测试
        boolean result = contentAuditService.deleteAuditResult(1L);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(contentAuditMapper).selectById(1L);
        verify(contentAuditMapper).deleteById(1L);
    }

    @Test
    void testDeleteAuditResult_NotFound() {
        // 准备测试数据
        when(contentAuditMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        boolean result = contentAuditService.deleteAuditResult(1L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(contentAuditMapper).selectById(1L);
        verify(contentAuditMapper, never()).deleteById(anyLong());
    }

    @Test
    void testBatchAuditContent_Success() {
        // 准备测试数据
        List<ContentAuditDTO> contentList = Arrays.asList(testContentAuditDTO);
        when(contentAuditMapper.insert(any(AuditResult.class))).thenReturn(1);

        // 执行测试
        List<AuditResult> result = contentAuditService.batchAuditContent(contentList);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testContentAuditDTO.getContent(), result.get(0).getContent());

        // 验证方法调用
        verify(contentAuditMapper).insert(any(AuditResult.class));
    }

    @Test
    void testBatchAuditContent_EmptyList() {
        // 准备测试数据
        List<ContentAuditDTO> contentList = Arrays.asList();

        // 执行测试
        List<AuditResult> result = contentAuditService.batchAuditContent(contentList);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.size());

        // 验证方法调用
        verify(contentAuditMapper, never()).insert(any(AuditResult.class));
    }

    @Test
    void testGetAuditHistory_Success() {
        // 准备测试数据
        List<AuditResult> auditResults = Arrays.asList(testAuditResult);
        when(contentAuditMapper.selectList(any())).thenReturn(auditResults);

        // 执行测试
        List<AuditResult> result = contentAuditService.getAuditHistory(1L, 10);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testAuditResult.getContent(), result.get(0).getContent());

        // 验证方法调用
        verify(contentAuditMapper).selectList(any());
    }

    @Test
    void testGetAuditHistory_Empty() {
        // 准备测试数据
        when(contentAuditMapper.selectList(any())).thenReturn(Arrays.asList());

        // 执行测试
        List<AuditResult> result = contentAuditService.getAuditHistory(1L, 10);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.size());

        // 验证方法调用
        verify(contentAuditMapper).selectList(any());
    }

    @Test
    void testIsContentSafe_Safe() {
        // 准备测试数据
        testContentAuditDTO.setContent("正常内容");

        // 执行测试
        boolean result = contentAuditService.isContentSafe(testContentAuditDTO);

        // 验证结果
        assertTrue(result);
    }

    @Test
    void testIsContentSafe_Unsafe() {
        // 准备测试数据
        testContentAuditDTO.setContent("极端内容");

        // 执行测试
        boolean result = contentAuditService.isContentSafe(testContentAuditDTO);

        // 验证结果
        assertFalse(result);
    }

    @Test
    void testGetContentRiskLevel_Low() {
        // 准备测试数据
        testContentAuditDTO.setContent("正常内容");

        // 执行测试
        int result = contentAuditService.getContentRiskLevel(testContentAuditDTO);

        // 验证结果
        assertEquals(0, result);
    }

    @Test
    void testGetContentRiskLevel_High() {
        // 准备测试数据
        testContentAuditDTO.setContent("极端内容");

        // 执行测试
        int result = contentAuditService.getContentRiskLevel(testContentAuditDTO);

        // 验证结果
        assertTrue(result > 0);
    }
}
