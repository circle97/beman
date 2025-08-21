package com.beman.service.impl;

import com.beman.mapper.AchievementMapper;
import com.beman.model.Achievement;
import com.beman.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 成就勋章服务实现类
 */
@Service
public class AchievementServiceImpl implements AchievementService {
    
    @Autowired
    private AchievementMapper achievementMapper;
    
    @Override
    public Achievement unlockAchievement(Long userId, String achievementType, String achievementName, String description) {
        // 检查是否已经解锁
        if (hasAchievement(userId, achievementType)) {
            return achievementMapper.selectByUserIdAndType(userId, achievementType).get(0);
        }
        
        // 创建新成就
        Achievement achievement = new Achievement(userId, achievementType, achievementName, description);
        achievement.setUnlockDate(LocalDateTime.now());
        achievement.setIconUrl(generateIconUrl(achievementType));
        
        // 保存到数据库
        achievementMapper.insert(achievement);
        
        return achievement;
    }
    
    @Override
    public Achievement getAchievementById(Long id) {
        return achievementMapper.selectById(id);
    }
    
    @Override
    public List<Achievement> getUserAchievements(Long userId) {
        return achievementMapper.selectByUserId(userId);
    }
    
    @Override
    public List<Achievement> getUserAchievementsByType(Long userId, String achievementType) {
        return achievementMapper.selectByUserIdAndType(userId, achievementType);
    }
    
    @Override
    public boolean hasAchievement(Long userId, String achievementType) {
        return achievementMapper.existsByUserIdAndType(userId, achievementType);
    }
    
    @Override
    public Map<String, Object> getUserAchievementStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 总成就数
        int totalAchievements = achievementMapper.countByUserId(userId);
        stats.put("totalAchievements", totalAchievements);
        
        // 按类型统计
        Map<String, Integer> typeStats = new HashMap<>();
        List<Achievement> allAchievements = getUserAchievements(userId);
        
        for (Achievement achievement : allAchievements) {
            String type = achievement.getAchievementType();
            typeStats.put(type, typeStats.getOrDefault(type, 0) + 1);
        }
        stats.put("typeStats", typeStats);
        
        // 解锁进度（假设每种类型最多有5个成就）
        Map<String, Double> progressStats = new HashMap<>();
        Map<String, Map<String, String>> availableTypes = getAvailableAchievementTypes();
        
        for (String type : availableTypes.keySet()) {
            int unlocked = typeStats.getOrDefault(type, 0);
            int total = availableTypes.get(type).size();
            double progress = total > 0 ? (double) unlocked / total * 100 : 0;
            progressStats.put(type, Math.round(progress * 100.0) / 100.0);
        }
        stats.put("progressStats", progressStats);
        
        return stats;
    }
    
    @Override
    public List<Achievement> autoCheckAndUnlockAchievements(Long userId) {
        List<Achievement> newAchievements = new ArrayList<>();
        
        // 这里应该根据用户的实际行为数据来检查成就
        // 例如：发帖数量、使用工具次数、完成计划数量等
        // 暂时返回空列表，后续可以扩展
        
        return newAchievements;
    }
    
    @Override
    public Map<String, Map<String, String>> getAvailableAchievementTypes() {
        Map<String, Map<String, String>> achievementTypes = new HashMap<>();
        
        // 沟通达人成就
        Map<String, String> communication = new HashMap<>();
        communication.put("first_post", "首次发帖");
        communication.put("active_commenter", "活跃评论者");
        communication.put("communication_master", "沟通达人");
        communication.put("helpful_advisor", "热心建议者");
        communication.put("community_builder", "社区建设者");
        achievementTypes.put("communication", communication);
        
        // 情绪管理成就
        Map<String, String> emotion = new HashMap<>();
        emotion.put("emotion_analyzer", "情绪分析师");
        emotion.put("self_reflection", "自我反思者");
        emotion.put("emotion_master", "情绪管理大师");
        emotion.put("calm_communicator", "冷静沟通者");
        emotion.put("emotional_growth", "情绪成长者");
        achievementTypes.put("emotion", emotion);
        
        // 关系经营成就
        Map<String, String> relationship = new HashMap<>();
        relationship.put("relationship_planner", "关系规划师");
        relationship.put("gift_giver", "礼物达人");
        relationship.put("date_organizer", "约会组织者");
        relationship.put("relationship_master", "关系经营大师");
        relationship.put("love_architect", "爱情建筑师");
        achievementTypes.put("relationship", relationship);
        
        // 个人成长成就
        Map<String, String> growth = new HashMap<>();
        growth.put("plan_follower", "计划执行者");
        growth.put("goal_achiever", "目标达成者");
        growth.put("continuous_learner", "持续学习者");
        growth.put("growth_master", "成长大师");
        growth.put("life_improver", "生活改善者");
        achievementTypes.put("growth", growth);
        
        return achievementTypes;
    }
    
    /**
     * 根据成就类型生成图标URL
     * @param achievementType 成就类型
     * @return 图标URL
     */
    private String generateIconUrl(String achievementType) {
        // 这里可以根据成就类型返回不同的图标
        // 暂时返回默认图标
        return "/images/achievements/" + achievementType + "_default.png";
    }
}
