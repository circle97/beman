package com.beman.controller;

import com.beman.model.Reminder;
import com.beman.model.dto.ReminderCreateDTO;
import com.beman.model.dto.ReminderQueryDTO;
import com.beman.service.ReminderService;
import com.beman.model.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 提醒管理控制器
 */
@RestController
@RequestMapping("/api/reminder")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    /**
     * 创建提醒
     */
    @PostMapping
    public Result<Reminder> createReminder(@Valid @RequestBody ReminderCreateDTO createDTO) {
        Reminder reminder = reminderService.createReminder(createDTO);
        return Result.success(reminder);
    }

    /**
     * 更新提醒
     */
    @PutMapping("/{id}")
    public Result<Reminder> updateReminder(@PathVariable Long id, @Valid @RequestBody ReminderCreateDTO updateDTO) {
        Reminder reminder = reminderService.updateReminder(id, updateDTO);
        return Result.success(reminder);
    }

    /**
     * 删除提醒
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteReminder(@PathVariable Long id) {
        reminderService.deleteReminder(id);
        return Result.success();
    }

    /**
     * 获取提醒详情
     */
    @GetMapping("/{id}")
    public Result<Reminder> getReminderDetail(@PathVariable Long id) {
        Reminder reminder = reminderService.getReminderDetail(id);
        return Result.success(reminder);
    }

    /**
     * 查询提醒列表
     */
    @GetMapping("/list")
    public Result<List<Reminder>> getReminderList(ReminderQueryDTO queryDTO) {
        List<Reminder> reminders = reminderService.getReminderList(queryDTO);
        return Result.success(reminders);
    }

    /**
     * 获取用户的提醒列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<Reminder>> getUserReminders(@PathVariable Long userId) {
        List<Reminder> reminders = reminderService.getUserReminders(userId);
        return Result.success(reminders);
    }

    /**
     * 标记提醒为已发送
     */
    @PostMapping("/{id}/mark-sent")
    public Result<Void> markAsSent(@PathVariable Long id) {
        reminderService.markAsSent(id);
        return Result.success();
    }

    /**
     * 标记提醒为发送失败
     */
    @PostMapping("/{id}/mark-failed")
    public Result<Void> markAsFailed(@PathVariable Long id, @RequestParam String reason) {
        reminderService.markAsFailed(id, reason);
        return Result.success();
    }

    /**
     * 重试发送提醒
     */
    @PostMapping("/{id}/retry")
    public Result<Void> retryReminder(@PathVariable Long id) {
        reminderService.retryReminder(id);
        return Result.success();
    }

    /**
     * 取消提醒
     */
    @PostMapping("/{id}/cancel")
    public Result<Void> cancelReminder(@PathVariable Long id) {
        reminderService.cancelReminder(id);
        return Result.success();
    }

    /**
     * 批量创建提醒（从日程创建）
     */
    @PostMapping("/batch")
    public Result<List<Reminder>> createRemindersFromSchedule(
            @RequestParam Long scheduleId, 
            @RequestBody List<ReminderCreateDTO> reminderSettings) {
        List<Reminder> reminders = reminderService.createRemindersFromSchedule(scheduleId, reminderSettings);
        return Result.success(reminders);
    }

    /**
     * 获取即将到期的提醒
     */
    @GetMapping("/upcoming")
    public Result<List<Reminder>> getUpcomingReminders(@RequestParam(defaultValue = "30") int minutes) {
        List<Reminder> reminders = reminderService.getUpcomingReminders(minutes);
        return Result.success(reminders);
    }

    /**
     * 处理提醒发送（定时任务调用）
     */
    @PostMapping("/process")
    public Result<Void> processReminders() {
        reminderService.processReminders();
        return Result.success();
    }

    /**
     * 获取提醒统计信息
     */
    @GetMapping("/stats")
    public Result<Object> getReminderStats() {
        Object stats = reminderService.getReminderStats();
        return Result.success(stats);
    }
}
