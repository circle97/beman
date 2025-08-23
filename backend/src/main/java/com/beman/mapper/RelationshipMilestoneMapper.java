package com.beman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beman.model.RelationshipMilestone;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 关系里程碑Mapper接口
 * 
 * @author beman
 * @since 2024-12-25
 */
@Mapper
public interface RelationshipMilestoneMapper extends BaseMapper<RelationshipMilestone> {

    /**
     * 根据用户ID和类型查询里程碑
     * 
     * @param userId 用户ID
     * @param milestoneType 里程碑类型
     * @return 里程碑列表
     */
    List<RelationshipMilestone> selectByUserIdAndType(@Param("userId") Long userId, @Param("milestoneType") String milestoneType);

    /**
     * 根据用户ID和日期范围查询里程碑
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 里程碑列表
     */
    List<RelationshipMilestone> selectByUserIdAndDateRange(@Param("userId") Long userId, 
                                                          @Param("startDate") LocalDate startDate, 
                                                          @Param("endDate") LocalDate endDate);

    /**
     * 根据用户ID查询里程碑统计
     * 
     * @param userId 用户ID
     * @return 统计信息
     */
    List<RelationshipMilestone> selectMilestoneStats(@Param("userId") Long userId);
}
