package com.beman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.model.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 日程Mapper接口
 */
@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

    /**
     * 分页查询日程
     */
    IPage<Schedule> selectSchedulePage(Page<Schedule> page, @Param("userId") Long userId,
                                      @Param("keyword") String keyword, @Param("type") Integer type,
                                      @Param("priority") Integer priority, @Param("status") Integer status,
                                      @Param("startTimeFrom") LocalDateTime startTimeFrom,
                                      @Param("startTimeTo") LocalDateTime startTimeTo,
                                      @Param("endTimeFrom") LocalDateTime endTimeTo,
                                      @Param("endTimeTo") LocalDateTime endTimeFrom,
                                      @Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo,
                                      @Param("tags") String tags, @Param("location") String location,
                                      @Param("includeRepeating") Boolean includeRepeating,
                                      @Param("sortBy") String sortBy, @Param("sortOrder") String sortOrder);

    /**
     * 获取指定日期的日程列表
     */
    List<Schedule> selectSchedulesByDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    /**
     * 获取指定日期范围的日程列表
     */
    List<Schedule> selectSchedulesByDateRange(@Param("userId") Long userId, 
                                             @Param("startDate") LocalDate startDate, 
                                             @Param("endDate") LocalDate endDate);

    /**
     * 获取即将到来的日程
     */
    List<Schedule> selectUpcomingSchedules(@Param("userId") Long userId, @Param("days") int days);

    /**
     * 获取重要日程（高优先级）
     */
    List<Schedule> selectImportantSchedules(@Param("userId") Long userId);

    /**
     * 获取今日日程数量
     */
    int selectTodayCount(@Param("userId") Long userId);

    /**
     * 获取本周日程数量
     */
    int selectWeekCount(@Param("userId") Long userId);

    /**
     * 获取本月日程数量
     */
    int selectMonthCount(@Param("userId") Long userId);

    /**
     * 获取已完成日程数量
     */
    int selectCompletedCount(@Param("userId") Long userId);

    /**
     * 获取待办日程数量
     */
    int selectPendingCount(@Param("userId") Long userId);

    /**
     * 获取高优先级日程数量
     */
    int selectHighPriorityCount(@Param("userId") Long userId);
}
