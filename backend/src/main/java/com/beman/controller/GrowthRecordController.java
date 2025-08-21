package com.beman.controller;

import com.beman.model.GrowthRecord;
import com.beman.service.GrowthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成长记录控制器
 * 提供成长记录的创建、查询和统计分析接口
 */
@RestController
@RequestMapping("/api/growth-records")
@CrossOrigin(origins = "*")
public class GrowthRecordController {
    
    @Autowired
    private GrowthRecordService growthRecordService;
    
    /**
     * 创建成长记录
     * @param record 成长记录
     * @return 创建结果
     */
    @PostMapping
    public Map<String, Object> createGrowthRecord(@RequestBody GrowthRecord record) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            GrowthRecord createdRecord = growthRecordService.createGrowthRecord(record);
            
            response.put("success", true);
            response.put("message", "成长记录创建成功");
            response.put("data", createdRecord);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "成长记录创建失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 根据ID查询成长记录
     * @param id 记录ID
     * @return 成长记录详情
     */
    @GetMapping("/{id}")
    public Map<String, Object> getGrowthRecordById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            GrowthRecord record = growthRecordService.getGrowthRecordById(id);
            
            if (record != null) {
                response.put("success", true);
                response.put("message", "获取成长记录成功");
                response.put("data", record);
            } else {
                response.put("success", false);
                response.put("message", "成长记录不存在");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取成长记录失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取用户的所有成长记录
     * @param userId 用户ID
     * @return 成长记录列表
     */
    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserGrowthRecords(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<GrowthRecord> records = growthRecordService.getUserGrowthRecords(userId);
            
            response.put("success", true);
            response.put("message", "获取成长记录列表成功");
            response.put("data", records);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取成长记录列表失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 根据类型获取用户的成长记录
     * @param userId 用户ID
     * @param growthType 成长类型
     * @return 成长记录列表
     */
    @GetMapping("/user/{userId}/type/{growthType}")
    public Map<String, Object> getUserGrowthRecordsByType(
            @PathVariable Long userId, 
            @PathVariable Integer growthType) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<GrowthRecord> records = growthRecordService.getUserGrowthRecordsByType(userId, growthType);
            
            response.put("success", true);
            response.put("message", "获取成长记录列表成功");
            response.put("data", records);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取成长记录列表失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 根据时间范围获取用户的成长记录
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 成长记录列表
     */
    @GetMapping("/user/{userId}/time-range")
    public Map<String, Object> getUserGrowthRecordsByTimeRange(
            @PathVariable Long userId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            LocalDateTime start = LocalDateTime.parse(startTime);
            LocalDateTime end = LocalDateTime.parse(endTime);
            
            List<GrowthRecord> records = growthRecordService.getUserGrowthRecordsByTimeRange(userId, start, end);
            
            response.put("success", true);
            response.put("message", "获取成长记录列表成功");
            response.put("data", records);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取成长记录列表失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 更新成长记录
     * @param id 记录ID
     * @param record 成长记录
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateGrowthRecord(@PathVariable Long id, @RequestBody GrowthRecord record) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            record.setId(id);
            GrowthRecord updatedRecord = growthRecordService.updateGrowthRecord(record);
            
            response.put("success", true);
            response.put("message", "成长记录更新成功");
            response.put("data", updatedRecord);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "成长记录更新失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 删除成长记录
     * @param id 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteGrowthRecord(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean deleted = growthRecordService.deleteGrowthRecord(id);
            
            if (deleted) {
                response.put("success", true);
                response.put("message", "成长记录删除成功");
            } else {
                response.put("success", false);
                response.put("message", "成长记录删除失败");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "成长记录删除失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取用户成长统计信息
     * @param userId 用户ID
     * @return 成长统计信息
     */
    @GetMapping("/user/{userId}/stats")
    public Map<String, Object> getUserGrowthStats(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> stats = growthRecordService.getUserGrowthStats(userId);
            
            response.put("success", true);
            response.put("message", "获取成长统计成功");
            response.put("data", stats);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取成长统计失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取用户成长分数趋势
     * @param userId 用户ID
     * @param days 天数
     * @return 成长分数趋势
     */
    @GetMapping("/user/{userId}/score-trend")
    public Map<String, Object> getUserScoreTrend(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "30") Integer days) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> trend = growthRecordService.getUserScoreTrend(userId, days);
            
            response.put("success", true);
            response.put("message", "获取成长分数趋势成功");
            response.put("data", trend);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取成长分数趋势失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取用户成长曲线数据
     * @param userId 用户ID
     * @param months 月数
     * @return 成长曲线数据
     */
    @GetMapping("/user/{userId}/growth-curve")
    public Map<String, Object> getUserGrowthCurve(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "6") Integer months) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> curve = growthRecordService.getUserGrowthCurve(userId, months);
            
            response.put("success", true);
            response.put("message", "获取成长曲线成功");
            response.put("data", curve);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取成长曲线失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 自动生成成长记录
     * @param userId 用户ID
     * @return 生成的成长记录列表
     */
    @PostMapping("/user/{userId}/auto-generate")
    public Map<String, Object> autoGenerateGrowthRecords(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<GrowthRecord> newRecords = growthRecordService.autoGenerateGrowthRecords(userId);
            
            response.put("success", true);
            response.put("message", "自动生成成长记录完成");
            response.put("data", newRecords);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "自动生成成长记录失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取成长类型映射
     * @return 成长类型映射
     */
    @GetMapping("/types")
    public Map<String, Object> getGrowthTypeMapping() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<Integer, String> types = growthRecordService.getGrowthTypeMapping();
            
            response.put("success", true);
            response.put("message", "获取成长类型映射成功");
            response.put("data", types);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取成长类型映射失败: " + e.getMessage());
        }
        
        return response;
    }
}
