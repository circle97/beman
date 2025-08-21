package com.beman.service;

import com.beman.model.Achievement;

import java.util.List;
import java.util.Map;

/**
 * 成就勋章服务接口
 * 负责成就勋章的解锁、查询和管理
 */
public interface AchievementService {
    
    /**
     * 解锁成就勋章
     * @param userId 用户ID
     * @param achievementType 成就类型
     * @param achievementName 成就名称
     * @param description 成就描述
     * @return 解锁的成就勋章
     */
    Achievement unlockAchievement(Long userId, String achievementType, String achievementName, String description);
    
    /**
     * 根据ID查询成就勋章
     * @param id 成就ID
     * @return 成就勋章
     */
    Achievement getAchievementById(Long id);
    
    /**
     * 获取用户的所有成就勋章
     * @param userId 用户ID
     * @return 成就勋章列表
     */
    List<Achievement> getUserAchievements(Long userId);
    
    /**
     * 根据类型获取用户的成就勋章
     * @param userId 用户ID
     * @param achievementType 成就类型
     * @return 成就勋章列表
     */
    List<Achievement> getUserAchievementsByType(Long userId, String achievementType);
    
    /**
     * 检查用户是否已解锁特定成就
     * @param userId 用户ID
     * @param achievementType 成就类型
     * @return 是否已解锁
     */
    boolean hasAchievement(Long userId, String achievementType);
    
    /**
     * 获取用户成就统计信息
     * @param userId 用户ID
     * @return 成就统计信息
     */
    Map<String, Object> getUserAchievementStats(Long userId);
    
    /**
     * 自动检查并解锁成就
     * 根据用户的行为数据自动解锁相应的成就
     * @param userId 用户ID
     * @return 新解锁的成就列表
     */
    List<Achievement> autoCheckAndUnlockAchievements(Long userId);
    
    /**
     * 获取所有可用的成就类型和描述
     * @return 成就类型映射
     */
    Map<String, Map<String, String>> getAvailableAchievementTypes();
}
