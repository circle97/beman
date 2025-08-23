package com.beman.service;

import com.beman.model.RelationshipMilestone;
import com.beman.model.GrowthTrajectory;
import com.beman.model.RelationshipGoal;
import com.beman.model.dto.MilestoneCreateDTO;
import com.beman.model.dto.TrajectoryCreateDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 成长档案服务接口
 * 
 * @author beman
 * @since 2024-12-25
 */
public interface GrowthArchiveService {

    // ==================== 里程碑管理 ====================
    
    /**
     * 创建里程碑
     * 
     * @param userId 用户ID
     * @param dto 里程碑创建DTO
     * @return 创建的里程碑
     */
    RelationshipMilestone createMilestone(Long userId, MilestoneCreateDTO dto);

    /**
     * 更新里程碑
     * 
     * @param userId 用户ID
     * @param milestoneId 里程碑ID
     * @param dto 里程碑创建DTO
     * @return 更新后的里程碑
     */
    RelationshipMilestone updateMilestone(Long userId, Long milestoneId, MilestoneCreateDTO dto);

    /**
     * 删除里程碑
     * 
     * @param userId 用户ID
     * @param milestoneId 里程碑ID
     * @return 是否删除成功
     */
    boolean deleteMilestone(Long userId, Long milestoneId);

    /**
     * 获取用户里程碑列表
     * 
     * @param userId 用户ID
     * @param milestoneType 里程碑类型（可选）
     * @return 里程碑列表
     */
    List<RelationshipMilestone> getUserMilestones(Long userId, String milestoneType);

    /**
     * 获取里程碑详情
     * 
     * @param userId 用户ID
     * @param milestoneId 里程碑ID
     * @return 里程碑详情
     */
    RelationshipMilestone getMilestoneDetail(Long userId, Long milestoneId);

    // ==================== 成长轨迹管理 ====================
    
    /**
     * 创建成长轨迹记录
     * 
     * @param userId 用户ID
     * @param dto 成长轨迹创建DTO
     * @return 创建的成长轨迹
     */
    GrowthTrajectory createTrajectory(Long userId, TrajectoryCreateDTO dto);

    /**
     * 更新成长轨迹记录
     * 
     * @param userId 用户ID
     * @param trajectoryId 成长轨迹ID
     * @param dto 成长轨迹创建DTO
     * @return 更新后的成长轨迹
     */
    GrowthTrajectory updateTrajectory(Long userId, Long trajectoryId, TrajectoryCreateDTO dto);

    /**
     * 获取用户成长轨迹
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 成长轨迹列表
     */
    List<GrowthTrajectory> getUserTrajectories(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 获取最新的成长轨迹
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 成长轨迹列表
     */
    List<GrowthTrajectory> getLatestTrajectories(Long userId, Integer limit);

    // ==================== 目标管理 ====================
    
    /**
     * 创建关系目标
     * 
     * @param userId 用户ID
     * @param goal 关系目标
     * @return 创建的目标
     */
    RelationshipGoal createGoal(Long userId, RelationshipGoal goal);

    /**
     * 更新目标进度
     * 
     * @param userId 用户ID
     * @param goalId 目标ID
     * @param progress 进度
     * @return 更新后的目标
     */
    RelationshipGoal updateGoalProgress(Long userId, Long goalId, Integer progress);

    /**
     * 完成目标
     * 
     * @param userId 用户ID
     * @param goalId 目标ID
     * @return 更新后的目标
     */
    RelationshipGoal completeGoal(Long userId, Long goalId);

    /**
     * 获取用户目标列表
     * 
     * @param userId 用户ID
     * @param status 目标状态（可选）
     * @return 目标列表
     */
    List<RelationshipGoal> getUserGoals(Long userId, Integer status);

    // ==================== 统计分析 ====================
    
    /**
     * 获取用户成长档案统计
     * 
     * @param userId 用户ID
     * @return 统计信息
     */
    Map<String, Object> getGrowthStats(Long userId);

    /**
     * 获取成长趋势分析
     * 
     * @param userId 用户ID
     * @param days 分析天数
     * @return 趋势数据
     */
    Map<String, Object> getGrowthTrend(Long userId, Integer days);

    /**
     * 获取里程碑统计
     * 
     * @param userId 用户ID
     * @return 里程碑统计
     */
    Map<String, Object> getMilestoneStats(Long userId);

    /**
     * 获取目标完成统计
     * 
     * @param userId 用户ID
     * @return 目标统计
     */
    Map<String, Object> getGoalStats(Long userId);
}
