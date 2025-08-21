package com.beman.service;

import com.beman.model.GrowthRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 成长记录服务接口
 * 负责成长记录的创建、查询和统计分析
 */
public interface GrowthRecordService {
    
    /**
     * 创建成长记录
     * @param record 成长记录
     * @return 创建的成长记录
     */
    GrowthRecord createGrowthRecord(GrowthRecord record);
    
    /**
     * 根据ID查询成长记录
     * @param id 记录ID
     * @return 成长记录
     */
    GrowthRecord getGrowthRecordById(Long id);
    
    /**
     * 获取用户的所有成长记录
     * @param userId 用户ID
     * @return 成长记录列表
     */
    List<GrowthRecord> getUserGrowthRecords(Long userId);
    
    /**
     * 根据类型获取用户的成长记录
     * @param userId 用户ID
     * @param growthType 成长类型
     * @return 成长记录列表
     */
    List<GrowthRecord> getUserGrowthRecordsByType(Long userId, Integer growthType);
    
    /**
     * 根据时间范围获取用户的成长记录
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 成长记录列表
     */
    List<GrowthRecord> getUserGrowthRecordsByTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 更新成长记录
     * @param record 成长记录
     * @return 更新后的成长记录
     */
    GrowthRecord updateGrowthRecord(GrowthRecord record);
    
    /**
     * 删除成长记录
     * @param id 记录ID
     * @return 是否删除成功
     */
    boolean deleteGrowthRecord(Long id);
    
    /**
     * 获取用户成长统计信息
     * @param userId 用户ID
     * @return 成长统计信息
     */
    Map<String, Object> getUserGrowthStats(Long userId);
    
    /**
     * 获取用户成长分数趋势
     * @param userId 用户ID
     * @param days 天数
     * @return 成长分数趋势
     */
    Map<String, Object> getUserScoreTrend(Long userId, Integer days);
    
    /**
     * 获取用户成长曲线数据
     * @param userId 用户ID
     * @param months 月数
     * @return 成长曲线数据
     */
    Map<String, Object> getUserGrowthCurve(Long userId, Integer months);
    
    /**
     * 自动生成成长记录
     * 根据用户的行为数据自动生成成长记录
     * @param userId 用户ID
     * @return 生成的成长记录列表
     */
    List<GrowthRecord> autoGenerateGrowthRecords(Long userId);
    
    /**
     * 获取成长类型映射
     * @return 成长类型映射
     */
    Map<Integer, String> getGrowthTypeMapping();
}
