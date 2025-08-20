package com.beman.service;

import com.beman.model.Reminder;
import com.beman.model.dto.ReminderCreateDTO;
import com.beman.model.dto.ReminderQueryDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 提醒服务接口
 */
public interface ReminderService {

    /**
     * 创建提醒
     */
    Reminder createReminder(ReminderCreateDTO createDTO);

    /**
     * 更新提醒
     */
    Reminder updateReminder(Long id, ReminderCreateDTO updateDTO);

    /**
     * 删除提醒
     */
    void deleteReminder(Long id);

    /**
     * 获取提醒详情
     */
    Reminder getReminderDetail(Long id);

    /**
     * 分页查询提醒
     */
    List<Reminder> getReminderList(ReminderQueryDTO queryDTO);

    /**
     * 获取需要发送的提醒列表
     */
    List<Reminder> getPendingReminders();

    /**
     * 获取用户的提醒列表
     */
    List<Reminder> getUserReminders(Long userId);

    /**
     * 标记提醒为已发送
     */
    void markAsSent(Long id);

    /**
     * 标记提醒为发送失败
     */
    void markAsFailed(Long id, String reason);

    /**
     * 重试发送提醒
     */
    void retryReminder(Long id);

    /**
     * 取消提醒
     */
    void cancelReminder(Long id);

    /**
     * 批量创建提醒（从日程创建）
     */
    List<Reminder> createRemindersFromSchedule(Long scheduleId, List<ReminderCreateDTO> reminderSettings);

    /**
     * 获取即将到期的提醒
     */
    List<Reminder> getUpcomingReminders(int minutes);

    /**
     * 处理提醒发送
     */
    void processReminders();

    /**
     * 获取提醒统计信息
     */
    ReminderStats getReminderStats();

    /**
     * 创建分级提醒
     * 根据日程优先级创建多个时间点的提醒
     */
    List<Reminder> createTieredReminders(Long scheduleId, Integer priority);

    /**
     * 获取分级提醒设置
     * 根据优先级返回提醒时间配置
     */
    List<TieredReminderConfig> getTieredReminderConfigs(Integer priority);

    /**
     * 发送分级提醒
     * 根据优先级和时间发送不同级别的提醒
     */
    void sendTieredReminders(Long scheduleId);

    /**
     * 获取提醒优先级配置
     */
    ReminderPriorityConfig getReminderPriorityConfig();

    /**
     * 提醒统计信息
     */
    interface ReminderStats {
        /**
         * 待发送提醒数量
         */
        int getPendingCount();

        /**
         * 已发送提醒数量
         */
        int getSentCount();

        /**
         * 发送失败提醒数量
         */
        int getFailedCount();

        /**
         * 今日发送提醒数量
         */
        int getTodaySentCount();

        /**
         * 本周发送提醒数量
         */
        int getWeekSentCount();
    }

    /**
     * 分级提醒配置
     */
    interface TieredReminderConfig {
        /**
         * 提醒级别：1-提前提醒，2-临近提醒，3-紧急提醒
         */
        int getLevel();

        /**
         * 提前时间（分钟）
         */
        int getAdvanceMinutes();

        /**
         * 提醒类型
         */
        int getType();

        /**
         * 提醒内容模板
         */
        String getContentTemplate();
    }

    /**
     * 提醒优先级配置
     */
    interface ReminderPriorityConfig {
        /**
         * 低优先级提醒配置
         */
        List<TieredReminderConfig> getLowPriorityConfig();

        /**
         * 中优先级提醒配置
         */
        List<TieredReminderConfig> getMediumPriorityConfig();

        /**
         * 高优先级提醒配置
         */
        List<TieredReminderConfig> getHighPriorityConfig();

        /**
         * 紧急优先级提醒配置
         */
        List<TieredReminderConfig> getUrgentPriorityConfig();
    }
}
