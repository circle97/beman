package com.beman.task;

import com.beman.service.ReminderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 提醒定时任务处理器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReminderTask {

    private final ReminderService reminderService;

    /**
     * 每分钟检查一次需要发送的提醒
     */
    @Scheduled(fixedRate = 60000) // 60秒 = 1分钟
    public void processReminders() {
        try {
            log.info("开始处理提醒任务...");
            reminderService.processReminders();
            log.info("提醒任务处理完成");
        } catch (Exception e) {
            log.error("处理提醒任务时发生错误", e);
        }
    }

    /**
     * 每天凌晨2点清理过期的提醒记录
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredReminders() {
        try {
            log.info("开始清理过期提醒记录...");
            // 这里可以添加清理逻辑，比如删除30天前的已发送提醒
            log.info("过期提醒记录清理完成");
        } catch (Exception e) {
            log.error("清理过期提醒记录时发生错误", e);
        }
    }

    /**
     * 每小时检查一次即将到期的提醒
     */
    @Scheduled(fixedRate = 3600000) // 3600000毫秒 = 1小时
    public void checkUpcomingReminders() {
        try {
            log.info("检查即将到期的提醒...");
            // 获取30分钟内到期的提醒
            Object upcomingReminders = reminderService.getUpcomingReminders(30);
            // 这里可以添加预提醒逻辑，比如提前通知用户
        } catch (Exception e) {
            log.error("检查即将到期的提醒时发生错误", e);
        }
    }
}
