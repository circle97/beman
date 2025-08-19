package com.beman.controller;

import com.beman.model.AuditResult;
import com.beman.service.ContentAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 内容审核控制器
 */
@RestController
@RequestMapping("/api/audit")
@CrossOrigin(origins = "*")
public class ContentAuditController {
    
    @Autowired
    private ContentAuditService contentAuditService;
    
    /**
     * 审核内容
     * @param request 包含content字段的请求体
     * @return 审核结果
     */
    @PostMapping("/content")
    public ResponseEntity<Map<String, Object>> auditContent(@RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            
            if (content == null || content.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "内容不能为空");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // 执行内容审核
            AuditResult result = contentAuditService.auditContent(content);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", result);
            response.put("message", "审核完成");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "审核失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 检查内容是否为极端内容
     * @param request 包含content字段的请求体
     * @return 检查结果
     */
    @PostMapping("/check-extreme")
    public ResponseEntity<Map<String, Object>> checkExtremeContent(@RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            
            if (content == null || content.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "内容不能为空");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            boolean isExtreme = contentAuditService.isExtremeContent(content);
            int riskLevel = contentAuditService.getRiskLevel(content);
            
            Map<String, Object> data = new HashMap<>();
            data.put("isExtreme", isExtreme);
            data.put("riskLevel", riskLevel);
            data.put("suggestion", isExtreme ? contentAuditService.getReplacementSuggestion(content) : null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", data);
            response.put("message", "检查完成");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "检查失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 获取内容风险等级
     * @param request 包含content字段的请求体
     * @return 风险等级
     */
    @PostMapping("/risk-level")
    public ResponseEntity<Map<String, Object>> getRiskLevel(@RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            
            if (content == null || content.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "内容不能为空");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            int riskLevel = contentAuditService.getRiskLevel(content);
            String riskDescription = getRiskDescription(riskLevel);
            
            Map<String, Object> data = new HashMap<>();
            data.put("riskLevel", riskLevel);
            data.put("riskDescription", riskDescription);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", data);
            response.put("message", "风险评估完成");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "评估失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 获取替换建议
     * @param request 包含content字段的请求体
     * @return 建议内容
     */
    @PostMapping("/suggestion")
    public ResponseEntity<Map<String, Object>> getSuggestion(@RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            
            if (content == null || content.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "内容不能为空");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            String suggestion = contentAuditService.getReplacementSuggestion(content);
            
            Map<String, Object> data = new HashMap<>();
            data.put("suggestion", suggestion);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", data);
            response.put("message", "获取建议成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取建议失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 获取风险等级描述
     */
    private String getRiskDescription(int riskLevel) {
        switch (riskLevel) {
            case 0:
                return "低风险";
            case 1:
                return "中风险";
            case 2:
                return "高风险";
            default:
                return "未知风险";
        }
    }
}
