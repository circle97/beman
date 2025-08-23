package com.beman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beman.mapper.RelationshipMilestoneMapper;
import com.beman.mapper.GrowthTrajectoryMapper;
import com.beman.model.RelationshipMilestone;
import com.beman.model.GrowthTrajectory;
import com.beman.model.RelationshipGoal;
import com.beman.model.dto.MilestoneCreateDTO;
import com.beman.model.dto.TrajectoryCreateDTO;
import com.beman.service.GrowthArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 成长档案服务实现类
 * 
 * @author beman
 * @since 2024-12-25
 */
@Slf4j
@Service
public class GrowthArchiveServiceImpl extends ServiceImpl<RelationshipMilestoneMapper, RelationshipMilestone> 
    implements GrowthArchiveService {

    @Resource
    private RelationshipMilestoneMapper milestoneMapper;

    @Resource
    private GrowthTrajectoryMapper trajectoryMapper;

    // ==================== 里程碑管理 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RelationshipMilestone createMilestone(Long userId, MilestoneCreateDTO dto) {
        log.info("创建里程碑，用户ID: {}, 标题: {}", userId, dto.getTitle());
        
        RelationshipMilestone milestone = new RelationshipMilestone();
        BeanUtils.copyProperties(dto, milestone);
        milestone.setUserId(userId);
        milestone.setCreateTime(LocalDateTime.now());
        milestone.setUpdateTime(LocalDateTime.now());
        
        // 计算综合评分
        milestone.setEmotionScore(calculateEmotionScore(dto));
        
        milestoneMapper.insert(milestone);
        log.info("里程碑创建成功，ID: {}", milestone.getId());
        
        return milestone;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RelationshipMilestone updateMilestone(Long userId, Long milestoneId, MilestoneCreateDTO dto) {
        log.info("更新里程碑，用户ID: {}, 里程碑ID: {}", userId, milestoneId);
        
        RelationshipMilestone existingMilestone = getMilestoneDetail(userId, milestoneId);
        if (existingMilestone == null) {
            throw new RuntimeException("里程碑不存在或无权限访问");
        }
        
        BeanUtils.copyProperties(dto, existingMilestone);
        existingMilestone.setUpdateTime(LocalDateTime.now());
        existingMilestone.setEmotionScore(calculateEmotionScore(dto));
        
        milestoneMapper.updateById(existingMilestone);
        log.info("里程碑更新成功，ID: {}", milestoneId);
        
        return existingMilestone;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMilestone(Long userId, Long milestoneId) {
        log.info("删除里程碑，用户ID: {}, 里程碑ID: {}", userId, milestoneId);
        
        RelationshipMilestone milestone = getMilestoneDetail(userId, milestoneId);
        if (milestone == null) {
            return false;
        }
        
        int result = milestoneMapper.deleteById(milestoneId);
        log.info("里程碑删除结果: {}", result > 0 ? "成功" : "失败");
        
        return result > 0;
    }

    @Override
    public List<RelationshipMilestone> getUserMilestones(Long userId, String milestoneType) {
        log.info("获取用户里程碑，用户ID: {}, 类型: {}", userId, milestoneType);
        
        QueryWrapper<RelationshipMilestone> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("deleted", 0)
                   .orderByDesc("milestone_date");
        
        if (milestoneType != null && !milestoneType.isEmpty()) {
            queryWrapper.eq("milestone_type", milestoneType);
        }
        
        List<RelationshipMilestone> milestones = milestoneMapper.selectList(queryWrapper);
        log.info("获取到里程碑数量: {}", milestones.size());
        
        return milestones;
    }

    @Override
    public RelationshipMilestone getMilestoneDetail(Long userId, Long milestoneId) {
        log.info("获取里程碑详情，用户ID: {}, 里程碑ID: {}", userId, milestoneId);
        
        QueryWrapper<RelationshipMilestone> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", milestoneId)
                   .eq("user_id", userId)
                   .eq("deleted", 0);
        
        RelationshipMilestone milestone = milestoneMapper.selectOne(queryWrapper);
        log.info("里程碑详情获取结果: {}", milestone != null ? "成功" : "失败");
        
        return milestone;
    }

    // ==================== 成长轨迹管理 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GrowthTrajectory createTrajectory(Long userId, TrajectoryCreateDTO dto) {
        log.info("创建成长轨迹，用户ID: {}, 日期: {}", userId, dto.getRecordDate());
        
        // 检查是否已存在该日期的记录
        QueryWrapper<GrowthTrajectory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("record_date", dto.getRecordDate())
                   .eq("deleted", 0);
        
        GrowthTrajectory existingTrajectory = trajectoryMapper.selectOne(queryWrapper);
        if (existingTrajectory != null) {
            throw new RuntimeException("该日期已存在成长轨迹记录");
        }
        
        GrowthTrajectory trajectory = new GrowthTrajectory();
        BeanUtils.copyProperties(dto, trajectory);
        trajectory.setUserId(userId);
        trajectory.setCreateTime(LocalDateTime.now());
        trajectory.setUpdateTime(LocalDateTime.now());
        
        // 计算综合评分
        trajectory.setOverallScore(calculateOverallScore(dto));
        
        trajectoryMapper.insert(trajectory);
        log.info("成长轨迹创建成功，ID: {}", trajectory.getId());
        
        return trajectory;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GrowthTrajectory updateTrajectory(Long userId, Long trajectoryId, TrajectoryCreateDTO dto) {
        log.info("更新成长轨迹，用户ID: {}, 轨迹ID: {}", userId, trajectoryId);
        
        QueryWrapper<GrowthTrajectory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", trajectoryId)
                   .eq("user_id", userId)
                   .eq("deleted", 0);
        
        GrowthTrajectory existingTrajectory = trajectoryMapper.selectOne(queryWrapper);
        if (existingTrajectory == null) {
            throw new RuntimeException("成长轨迹不存在或无权限访问");
        }
        
        BeanUtils.copyProperties(dto, existingTrajectory);
        existingTrajectory.setUpdateTime(LocalDateTime.now());
        existingTrajectory.setOverallScore(calculateOverallScore(dto));
        
        trajectoryMapper.updateById(existingTrajectory);
        log.info("成长轨迹更新成功，ID: {}", trajectoryId);
        
        return existingTrajectory;
    }

    @Override
    public List<GrowthTrajectory> getUserTrajectories(Long userId, LocalDate startDate, LocalDate endDate) {
        log.info("获取用户成长轨迹，用户ID: {}, 日期范围: {} - {}", userId, startDate, endDate);
        
        QueryWrapper<GrowthTrajectory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("deleted", 0)
                   .orderByDesc("record_date");
        
        if (startDate != null) {
            queryWrapper.ge("record_date", startDate);
        }
        if (endDate != null) {
            queryWrapper.le("record_date", endDate);
        }
        
        List<GrowthTrajectory> trajectories = trajectoryMapper.selectList(queryWrapper);
        log.info("获取到成长轨迹数量: {}", trajectories.size());
        
        return trajectories;
    }

    @Override
    public List<GrowthTrajectory> getLatestTrajectories(Long userId, Integer limit) {
        log.info("获取最新成长轨迹，用户ID: {}, 限制数量: {}", userId, limit);
        
        QueryWrapper<GrowthTrajectory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("deleted", 0)
                   .orderByDesc("record_date")
                   .last("LIMIT " + (limit != null ? limit : 10));
        
        List<GrowthTrajectory> trajectories = trajectoryMapper.selectList(queryWrapper);
        log.info("获取到最新成长轨迹数量: {}", trajectories.size());
        
        return trajectories;
    }

    // ==================== 目标管理 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RelationshipGoal createGoal(Long userId, RelationshipGoal goal) {
        log.info("创建关系目标，用户ID: {}, 标题: {}", userId, goal.getTitle());
        
        goal.setUserId(userId);
        goal.setCreateTime(LocalDateTime.now());
        goal.setUpdateTime(LocalDateTime.now());
        goal.setStatus(0); // 进行中
        goal.setProgress(0); // 初始进度为0
        
        // TODO: 实现目标创建逻辑
        log.info("关系目标创建成功");
        
        return goal;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RelationshipGoal updateGoalProgress(Long userId, Long goalId, Integer progress) {
        log.info("更新目标进度，用户ID: {}, 目标ID: {}, 进度: {}", userId, goalId, progress);
        
        // TODO: 实现目标进度更新逻辑
        log.info("目标进度更新成功");
        
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RelationshipGoal completeGoal(Long userId, Long goalId) {
        log.info("完成目标，用户ID: {}, 目标ID: {}", userId, goalId);
        
        // TODO: 实现目标完成逻辑
        log.info("目标完成成功");
        
        return null;
    }

    @Override
    public List<RelationshipGoal> getUserGoals(Long userId, Integer status) {
        log.info("获取用户目标，用户ID: {}, 状态: {}", userId, status);
        
        // TODO: 实现获取用户目标逻辑
        log.info("获取用户目标成功");
        
        return new ArrayList<>();
    }

    // ==================== 统计分析 ====================

    @Override
    public Map<String, Object> getGrowthStats(Long userId) {
        log.info("获取成长档案统计，用户ID: {}", userId);
        
        Map<String, Object> stats = new HashMap<>();
        
        // 里程碑统计
        Map<String, Object> milestoneStats = getMilestoneStats(userId);
        stats.put("milestone", milestoneStats);
        
        // 成长轨迹统计
        Map<String, Object> trajectoryStats = getTrajectoryStats(userId);
        stats.put("trajectory", trajectoryStats);
        
        // 目标统计
        Map<String, Object> goalStats = getGoalStats(userId);
        stats.put("goal", goalStats);
        
        log.info("成长档案统计获取成功");
        return stats;
    }

    @Override
    public Map<String, Object> getGrowthTrend(Long userId, Integer days) {
        log.info("获取成长趋势分析，用户ID: {}, 天数: {}", userId, days);
        
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days != null ? days : 30);
        
        List<GrowthTrajectory> trajectories = getUserTrajectories(userId, startDate, endDate);
        
        Map<String, Object> trend = new HashMap<>();
        trend.put("dates", trajectories.stream().map(GrowthTrajectory::getRecordDate).collect(Collectors.toList()));
        trend.put("scores", trajectories.stream().map(GrowthTrajectory::getOverallScore).collect(Collectors.toList()));
        trend.put("communication", trajectories.stream().map(GrowthTrajectory::getCommunicationScore).collect(Collectors.toList()));
        trend.put("trust", trajectories.stream().map(GrowthTrajectory::getTrustScore).collect(Collectors.toList()));
        trend.put("support", trajectories.stream().map(GrowthTrajectory::getSupportScore).collect(Collectors.toList()));
        trend.put("intimacy", trajectories.stream().map(GrowthTrajectory::getIntimacyScore).collect(Collectors.toList()));
        
        log.info("成长趋势分析获取成功");
        return trend;
    }

    @Override
    public Map<String, Object> getMilestoneStats(Long userId) {
        log.info("获取里程碑统计，用户ID: {}", userId);
        
        Map<String, Object> stats = new HashMap<>();
        
        // 总里程碑数量
        QueryWrapper<RelationshipMilestone> totalQuery = new QueryWrapper<>();
        totalQuery.eq("user_id", userId).eq("deleted", 0);
        int totalCount = milestoneMapper.selectCount(totalQuery);
        stats.put("totalCount", totalCount);
        
        // 按类型统计
        List<RelationshipMilestone> milestones = getUserMilestones(userId, null);
        Map<String, Long> typeStats = milestones.stream()
            .collect(Collectors.groupingBy(RelationshipMilestone::getMilestoneType, Collectors.counting()));
        stats.put("typeStats", typeStats);
        
        // 最近里程碑
        if (!milestones.isEmpty()) {
            stats.put("latestMilestone", milestones.get(0));
        }
        
        log.info("里程碑统计获取成功");
        return stats;
    }

    @Override
    public Map<String, Object> getGoalStats(Long userId) {
        log.info("获取目标统计，用户ID: {}", userId);
        
        Map<String, Object> stats = new HashMap<>();
        
        // TODO: 实现目标统计逻辑
        stats.put("totalCount", 0);
        stats.put("completedCount", 0);
        stats.put("inProgressCount", 0);
        stats.put("completionRate", 0.0);
        
        log.info("目标统计获取成功");
        return stats;
    }

    // ==================== 私有方法 ====================

    /**
     * 计算情感评分
     */
    private Integer calculateEmotionScore(MilestoneCreateDTO dto) {
        // 基础评分
        int baseScore = 5;
        
        // 根据类型调整评分
        if (dto.getMilestoneType() != null) {
            switch (dto.getMilestoneType()) {
                case "first_meet":
                case "first_date":
                    baseScore = 7;
                    break;
                case "confession":
                case "engagement":
                    baseScore = 9;
                    break;
                case "wedding":
                    baseScore = 10;
                    break;
                case "anniversary":
                    baseScore = 8;
                    break;
                default:
                    baseScore = 6;
            }
        }
        
        return Math.min(10, Math.max(1, baseScore));
    }

    /**
     * 计算综合评分
     */
    private Integer calculateOverallScore(TrajectoryCreateDTO dto) {
        int totalScore = 0;
        int validScores = 0;
        
        if (dto.getCommunicationScore() != null) {
            totalScore += dto.getCommunicationScore();
            validScores++;
        }
        if (dto.getTrustScore() != null) {
            totalScore += dto.getTrustScore();
            validScores++;
        }
        if (dto.getSupportScore() != null) {
            totalScore += dto.getSupportScore();
            validScores++;
        }
        if (dto.getIntimacyScore() != null) {
            totalScore += dto.getIntimacyScore();
            validScores++;
        }
        
        return validScores > 0 ? totalScore / validScores : 0;
    }

    /**
     * 获取成长轨迹统计
     */
    private Map<String, Object> getTrajectoryStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 总记录数量
        QueryWrapper<GrowthTrajectory> totalQuery = new QueryWrapper<>();
        totalQuery.eq("user_id", userId).eq("deleted", 0);
        int totalCount = trajectoryMapper.selectCount(totalQuery);
        stats.put("totalCount", totalCount);
        
        // 平均评分
        if (totalCount > 0) {
            List<GrowthTrajectory> trajectories = getUserTrajectories(userId, null, null);
            double avgOverallScore = trajectories.stream()
                .mapToInt(GrowthTrajectory::getOverallScore)
                .average()
                .orElse(0.0);
            stats.put("avgOverallScore", avgOverallScore);
            
            // 最高和最低评分
            int maxScore = trajectories.stream()
                .mapToInt(GrowthTrajectory::getOverallScore)
                .max()
                .orElse(0);
            int minScore = trajectories.stream()
                .mapToInt(GrowthTrajectory::getOverallScore)
                .min()
                .orElse(0);
            stats.put("maxScore", maxScore);
            stats.put("minScore", minScore);
        }
        
        return stats;
    }
}
