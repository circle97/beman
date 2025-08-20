package com.beman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beman.model.Reminder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 提醒Mapper接口
 */
@Mapper
public interface ReminderMapper extends BaseMapper<Reminder> {

    /**
     * 获取待发送的提醒列表
     */
    List<Reminder> selectPendingReminders();

    /**
     * 获取用户的提醒列表
     */
    List<Reminder> selectUserReminders(@Param("userId") Long userId);

    /**
     * 获取即将到期的提醒
     */
    List<Reminder> selectUpcomingReminders(@Param("minutes") int minutes);

    /**
     * 获取待发送提醒数量
     */
    int selectPendingCount();

    /**
     * 获取已发送提醒数量
     */
    int selectSentCount();

    /**
     * 获取发送失败提醒数量
     */
    int selectFailedCount();

    /**
     * 获取今日发送提醒数量
     */
    int selectTodaySentCount();

    /**
     * 获取本周发送提醒数量
     */
    int selectWeekSentCount();

    /**
     * 根据日程ID获取提醒列表
     */
    List<Reminder> selectByScheduleId(@Param("scheduleId") Long scheduleId);
}
