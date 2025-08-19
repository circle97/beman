package com.beman.controller;

import com.beman.model.AuditResult;
import com.beman.service.ContentAuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContentAuditController.class)
@DisplayName("内容审核控制器测试")
class ContentAuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentAuditService contentAuditService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuditResult mockAuditResult;

    @BeforeEach
    void setUp() {
        mockAuditResult = new AuditResult(true, "建议先尝试沟通和改善");
        mockAuditResult.setRiskLevel(2);
        mockAuditResult.setAuditStatus(2);
        mockAuditResult.setRejectReason("包含极端内容");
    }

    @Test
    @DisplayName("测试内容审核API - 成功")
    void testAuditContentSuccess() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("content", "我要和她分手");

        when(contentAuditService.auditContent(anyString())).thenReturn(mockAuditResult);

        mockMvc.perform(post("/api/audit/content")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.isExtreme").value(true));
    }

    @Test
    @DisplayName("测试内容审核API - 空内容")
    void testAuditContentEmptyContent() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("content", "");

        mockMvc.perform(post("/api/audit/content")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("测试检查极端内容API - 成功")
    void testCheckExtremeContentSuccess() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("content", "我要和她分手");

        when(contentAuditService.isExtremeContent(anyString())).thenReturn(true);
        when(contentAuditService.getRiskLevel(anyString())).thenReturn(2);
        when(contentAuditService.getReplacementSuggestion(anyString())).thenReturn("建议先尝试沟通");

        mockMvc.perform(post("/api/audit/check-extreme")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isExtreme").value(true));
    }

    @Test
    @DisplayName("测试风险等级API - 成功")
    void testGetRiskLevelSuccess() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("content", "最近吵架了，很生气");

        when(contentAuditService.getRiskLevel(anyString())).thenReturn(1);

        mockMvc.perform(post("/api/audit/risk-level")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.riskLevel").value(1));
    }

    @Test
    @DisplayName("测试建议API - 成功")
    void testGetSuggestionSuccess() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("content", "我想和她分手");

        when(contentAuditService.getReplacementSuggestion(anyString())).thenReturn("建议先尝试沟通和改善");

        mockMvc.perform(post("/api/audit/suggestion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.suggestion").value("建议先尝试沟通和改善"));
    }
}
