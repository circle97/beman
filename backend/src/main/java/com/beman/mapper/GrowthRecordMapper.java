package com.beman.mapper;

import com.beman.model.GrowthRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 成长记录数据访问接口
 */
@Mapper
public interface GrowthRecordMapper {
    
    /**
     * 插入成长记录
     * @param record 成长记录
     * @return 影响行数
     */
    int insert(GrowthRecord record);
    
    /**
     * 根据ID查询成长记录
     * @param id 记录ID
     * @return 成长记录
     */
    GrowthRecord selectById(@Param("id") Long id);
    
    /**
     * 根据用户ID查询所有成长记录
     * @param userId 用户ID
     * @return 成长记录列表
     */
    List<GrowthRecord> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID和成长类型查询成长记录
     * @param userId 用户ID
     * @param growthType 成长类型
     * @return 成长记录列表
     */
    List<GrowthRecord> selectByUserIdAndType(@Param("userId") Long userId, @Param("growthType") Integer growthType);
    
    /**
     * 根据用户ID和时间范围查询成长记录
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 成长记录列表
     */
    List<GrowthRecord> selectByUserIdAndTimeRange(
        @Param("userId") Long userId, 
        @Param("startTime") LocalDateTime startTime, 
        @Param("endTime") LocalDateTime endTime);
    
    /**
     * 更新成长记录
     * @param record 成长记录
     * @return 影响行数
     */
    int update(GrowthRecord record);
    
    /**
     * 根据ID删除成长记录（逻辑删除）
     * @param id 记录ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 统计用户成长记录数量
     * @param userId 用户ID
     * @return 记录数量
     */
    int countByUserId(@Param("userId") Long userId);
    
    /**
     * 统计用户各类型成长记录数量
     * @param userId 用户ID
     * @return 各类型记录数量
     */
    List<GrowthRecord> countByUserIdAndType(@Param("userId") Long userId);
    
    /**
     * 获取用户成长分数趋势
     * @param userId 用户ID
     * @param days 天数
     * @return 成长分数趋势
     */
    List<GrowthRecord> getScoreTrend(@Param("userId") Long userId, @Param("days") Integer days);
}
