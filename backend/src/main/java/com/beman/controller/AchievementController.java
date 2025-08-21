package com.beman.controller;

import com.beman.model.Achievement;
import com.beman.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成就勋章控制器
 * 提供成就勋章的解锁、查询和管理接口
 */
@RestController
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "*")
public class AchievementController {
    
    @Autowired
    private AchievementService achievementService;
    
    /**
     * 解锁成就勋章
     * @param request 解锁请求
     * @return 解锁结果
     */
    @PostMapping("/unlock")
    public Map<String, Object> unlockAchievement(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = Long.parseLong(request.get("userId").toString());
            String achievementType = request.get("achievementType").toString();
            String achievementName = request.get("achievementName").toString();
            String description = request.get("description").toString();
            
            Achievement achievement = achievementService.unlockAchievement(
                userId, achievementType, achievementName, description);
            
            response.put("success", true);
            response.put("message", "成就解锁成功");
            response.put("data", achievement);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "成就解锁失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取用户的所有成就勋章
     * @param userId 用户ID
     * @return 成就勋章列表
     */
    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserAchievements(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Achievement> achievements = achievementService.getUserAchievements(userId);
            
            response.put("success", true);
            response.put("message", "获取成就列表成功");
            response.put("data", achievements);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取成就列表失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 根据类型获取用户的成就勋章
     * @param userId 用户ID
     * @param achievementType 成就类型
     * @return 成就勋章列表
     */
    @GetMapping("/user/{userId}/type/{achievementType}")
    public Map<String, Object> getUserAchievementsByType(
            @PathVariable Long userId, 
            @PathVariable String achievementType) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Achievement> achievements = achievementService.getUserAchievementsByType(userId, achievementType);
            
            response.put("success", true);
            response.put("message", "获取成就列表成功");
            response.put("data", achievements);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取成就列表失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取用户成就统计信息
     * @param userId 用户ID
     * @return 成就统计信息
     */
    @GetMapping("/user/{userId}/stats")
    public Map<String, Object> getUserAchievementStats(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> stats = achievementService.getUserAchievementStats(userId);
            
            response.put("success", true);
            response.put("message", "获取成就统计成功");
            response.put("data", stats);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取成就统计失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 检查用户是否已解锁特定成就
     * @param userId 用户ID
     * @param achievementType 成就类型
     * @return 检查结果
     */
    @GetMapping("/user/{userId}/check/{achievementType}")
    public Map<String, Object> checkAchievement(
            @PathVariable Long userId, 
            @PathVariable String achievementType) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean hasAchievement = achievementService.hasAchievement(userId, achievementType);
            
            response.put("success", true);
            response.put("message", "检查成就状态成功");
            response.put("data", hasAchievement);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "检查成就状态失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 自动检查并解锁成就
     * @param userId 用户ID
     * @return 新解锁的成就列表
     */
    @PostMapping("/user/{userId}/auto-check")
    public Map<String, Object> autoCheckAndUnlockAchievements(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Achievement> newAchievements = achievementService.autoCheckAndUnlockAchievements(userId);
            
            response.put("success", true);
            response.put("message", "自动检查成就完成");
            response.put("data", newAchievements);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "自动检查成就失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取所有可用的成就类型和描述
     * @return 成就类型映射
     */
    @GetMapping("/types")
    public Map<String, Object> getAvailableAchievementTypes() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Map<String, String>> types = achievementService.getAvailableAchievementTypes();
            
            response.put("success", true);
            response.put("message", "获取成就类型成功");
            response.put("data", types);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取成就类型失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 根据ID获取成就勋章详情
     * @param id 成就ID
     * @return 成就勋章详情
     */
    @GetMapping("/{id}")
    public Map<String, Object> getAchievementById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Achievement achievement = achievementService.getAchievementById(id);
            
            if (achievement != null) {
                response.put("success", true);
                response.put("message", "获取成就详情成功");
                response.put("data", achievement);
            } else {
                response.put("success", false);
                response.put("message", "成就不存在");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取成就详情失败: " + e.getMessage());
        }
        
        return response;
    }
}
