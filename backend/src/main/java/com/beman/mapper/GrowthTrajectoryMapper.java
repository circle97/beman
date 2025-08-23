package com.beman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beman.model.GrowthTrajectory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 成长轨迹Mapper接口
 * 
 * @author beman
 * @since 2024-12-25
 */
@Mapper
public interface GrowthTrajectoryMapper extends BaseMapper<GrowthTrajectory> {

    /**
     * 根据用户ID和日期范围查询成长轨迹
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 成长轨迹列表
     */
    List<GrowthTrajectory> selectByUserIdAndDateRange(@Param("userId") Long userId, 
                                                     @Param("startDate") LocalDate startDate, 
                                                     @Param("endDate") LocalDate endDate);

    /**
     * 根据用户ID查询最新的成长轨迹
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 成长轨迹列表
     */
    List<GrowthTrajectory> selectLatestByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    /**
     * 根据用户ID查询成长轨迹统计
     * 
     * @param userId 用户ID
     * @return 统计信息
     */
    List<GrowthTrajectory> selectTrajectoryStats(@Param("userId") Long userId);

    /**
     * 根据用户ID查询平均评分
     * 
     * @param userId 用户ID
     * @return 平均评分
     */
    GrowthTrajectory selectAverageScores(@Param("userId") Long userId);
}
