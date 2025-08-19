package com.beman.controller;

import com.beman.model.PracticePlan;
import com.beman.service.PracticePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实践计划控制器
 */
@RestController
@RequestMapping("/api/practice-plan")
@CrossOrigin(origins = "*")
public class PracticePlanController {
    
    @Autowired
    private PracticePlanService practicePlanService;
    
    /**
     * 创建实践计划
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPlan(@RequestBody PracticePlan plan) {
        try {
            PracticePlan createdPlan = practicePlanService.createPlan(plan);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", createdPlan);
            response.put("message", "实践计划创建成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "创建失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 更新实践计划
     */
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updatePlan(@RequestBody PracticePlan plan) {
        try {
            PracticePlan updatedPlan = practicePlanService.updatePlan(plan);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updatedPlan);
            response.put("message", "实践计划更新成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 获取用户的实践计划列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserPlans(@PathVariable Long userId) {
        try {
            List<PracticePlan> plans = practicePlanService.getUserPlans(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", plans);
            response.put("message", "获取成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 获取实践计划详情
     */
    @GetMapping("/{planId}")
    public ResponseEntity<Map<String, Object>> getPlanById(@PathVariable Long planId) {
        try {
            PracticePlan plan = practicePlanService.getPlanById(planId);
            
            if (plan == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "实践计划不存在");
                return ResponseEntity.notFound().build();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", plan);
            response.put("message", "获取成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 删除实践计划
     */
    @DeleteMapping("/{planId}")
    public ResponseEntity<Map<String, Object>> deletePlan(@PathVariable Long planId) {
        try {
            boolean success = practicePlanService.deletePlan(planId);
            
            if (!success) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "实践计划不存在");
                return ResponseEntity.notFound().build();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "删除成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 更新计划进度
     */
    @PutMapping("/{planId}/progress")
    public ResponseEntity<Map<String, Object>> updateProgress(
            @PathVariable Long planId,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer progress = request.get("progress");
            if (progress == null || progress < 0 || progress > 100) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "进度值必须在0-100之间");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            boolean success = practicePlanService.updateProgress(planId, progress);
            
            if (!success) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "实践计划不存在");
                return ResponseEntity.notFound().build();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "进度更新成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 更新计划状态
     */
    @PutMapping("/{planId}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Long planId,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer status = request.get("status");
            if (status == null || status < 0 || status > 3) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "状态值无效");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            boolean success = practicePlanService.updateStatus(planId, status);
            
            if (!success) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "实践计划不存在");
                return ResponseEntity.notFound().build();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "状态更新成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 获取用户公开的计划列表
     */
    @GetMapping("/user/{userId}/public")
    public ResponseEntity<Map<String, Object>> getPublicPlans(@PathVariable Long userId) {
        try {
            List<PracticePlan> plans = practicePlanService.getPublicPlans(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", plans);
            response.put("message", "获取成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 获取计划统计信息
     */
    @GetMapping("/user/{userId}/statistics")
    public ResponseEntity<Map<String, Object>> getPlanStatistics(@PathVariable Long userId) {
        try {
            PracticePlanService.PlanStatistics statistics = practicePlanService.getPlanStatistics(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", statistics);
            response.put("message", "获取成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
