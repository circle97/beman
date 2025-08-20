package com.beman.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beman.model.Schedule;
import com.beman.model.dto.ScheduleCreateDTO;
import com.beman.model.dto.ScheduleQueryDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * 日程服务接口
 */
public interface ScheduleService {

    /**
     * 创建日程
     */
    Schedule createSchedule(ScheduleCreateDTO createDTO);

    /**
     * 更新日程
     */
    Schedule updateSchedule(Long id, ScheduleCreateDTO updateDTO);

    /**
     * 删除日程
     */
    void deleteSchedule(Long id);

    /**
     * 获取日程详情
     */
    Schedule getScheduleDetail(Long id);

    /**
     * 分页查询日程
     */
    IPage<Schedule> getSchedulePage(ScheduleQueryDTO queryDTO);

    /**
     * 获取指定日期的日程列表
     */
    List<Schedule> getSchedulesByDate(LocalDate date);

    /**
     * 获取指定日期范围的日程列表
     */
    List<Schedule> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * 获取即将到来的日程（用于提醒）
     */
    List<Schedule> getUpcomingSchedules(int days);

    /**
     * 获取重要日程（高优先级）
     */
    List<Schedule> getImportantSchedules();

    /**
     * 完成日程
     */
    void completeSchedule(Long id);

    /**
     * 取消日程
     */
    void cancelSchedule(Long id);

    /**
     * 智能识别重要日期
     * 从文本中识别生日、纪念日等重要日期
     */
    List<Schedule> identifyImportantDates(String text);

    /**
     * 生成重复日程
     * 根据重复规则生成未来的日程实例
     */
    List<Schedule> generateRepeatingSchedules(Long scheduleId, LocalDate untilDate);

    /**
     * 获取日程统计信息
     */
    ScheduleStats getScheduleStats();

    /**
     * 日程统计信息
     */
    interface ScheduleStats {
        /**
         * 今日日程数量
         */
        int getTodayCount();

        /**
         * 本周日程数量
         */
        int getWeekCount();

        /**
         * 本月日程数量
         */
        int getMonthCount();

        /**
         * 已完成日程数量
         */
        int getCompletedCount();

        /**
         * 待办日程数量
         */
        int getPendingCount();

        /**
         * 高优先级日程数量
         */
        int getHighPriorityCount();
    }
}
