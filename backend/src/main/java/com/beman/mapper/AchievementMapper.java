package com.beman.mapper;

import com.beman.model.Achievement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 成就勋章数据访问接口
 */
@Mapper
public interface AchievementMapper {
    
    /**
     * 插入成就勋章
     * @param achievement 成就勋章
     * @return 影响行数
     */
    int insert(Achievement achievement);
    
    /**
     * 根据ID查询成就勋章
     * @param id 成就ID
     * @return 成就勋章
     */
    Achievement selectById(@Param("id") Long id);
    
    /**
     * 根据用户ID查询所有成就勋章
     * @param userId 用户ID
     * @return 成就勋章列表
     */
    List<Achievement> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID和成就类型查询成就勋章
     * @param userId 用户ID
     * @param achievementType 成就类型
     * @return 成就勋章列表
     */
    List<Achievement> selectByUserIdAndType(@Param("userId") Long userId, @Param("achievementType") String achievementType);
    
    /**
     * 更新成就勋章
     * @param achievement 成就勋章
     * @return 影响行数
     */
    int update(Achievement achievement);
    
    /**
     * 根据ID删除成就勋章（逻辑删除）
     * @param id 成就ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 检查用户是否已解锁特定成就
     * @param userId 用户ID
     * @param achievementType 成就类型
     * @return 是否已解锁
     */
    boolean existsByUserIdAndType(@Param("userId") Long userId, @Param("achievementType") String achievementType);
    
    /**
     * 统计用户解锁的成就数量
     * @param userId 用户ID
     * @return 成就数量
     */
    int countByUserId(@Param("userId") Long userId);
}
