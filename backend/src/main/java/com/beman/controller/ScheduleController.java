package com.beman.controller;

import com.beman.model.Schedule;
import com.beman.model.dto.ScheduleCreateDTO;
import com.beman.model.dto.ScheduleQueryDTO;
import com.beman.service.ScheduleService;
import com.beman.model.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import com.beman.service.ReminderService;
import com.beman.service.GiftRecommendationService;
import java.math.BigDecimal;
import com.beman.model.Reminder;
import com.beman.model.GiftRecommendation;

/**
 * 日程管理控制器
 */
@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ReminderService reminderService;
    private final GiftRecommendationService giftRecommendationService;

    /**
     * 创建日程
     */
    @PostMapping
    public Result<Schedule> createSchedule(@Valid @RequestBody ScheduleCreateDTO createDTO) {
        Schedule schedule = scheduleService.createSchedule(createDTO);
        return Result.success(schedule);
    }

    /**
     * 更新日程
     */
    @PutMapping("/{id}")
    public Result<Schedule> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleCreateDTO updateDTO) {
        Schedule schedule = scheduleService.updateSchedule(id, updateDTO);
        return Result.success(schedule);
    }

    /**
     * 删除日程
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return Result.success();
    }

    /**
     * 获取日程详情
     */
    @GetMapping("/{id}")
    public Result<Schedule> getScheduleDetail(@PathVariable Long id) {
        Schedule schedule = scheduleService.getScheduleDetail(id);
        return Result.success(schedule);
    }

    /**
     * 分页查询日程
     */
    @GetMapping("/page")
    public Result<Object> getSchedulePage(ScheduleQueryDTO queryDTO) {
        Object pageResult = scheduleService.getSchedulePage(queryDTO);
        return Result.success(pageResult);
    }

    /**
     * 获取指定日期的日程列表
     */
    @GetMapping("/date/{date}")
    public Result<List<Schedule>> getSchedulesByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Schedule> schedules = scheduleService.getSchedulesByDate(localDate);
        return Result.success(schedules);
    }

    /**
     * 获取指定日期范围的日程列表
     */
    @GetMapping("/range")
    public Result<List<Schedule>> getSchedulesByDateRange(
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<Schedule> schedules = scheduleService.getSchedulesByDateRange(start, end);
        return Result.success(schedules);
    }

    /**
     * 获取即将到来的日程
     */
    @GetMapping("/upcoming")
    public Result<List<Schedule>> getUpcomingSchedules(@RequestParam(defaultValue = "7") int days) {
        List<Schedule> schedules = scheduleService.getUpcomingSchedules(days);
        return Result.success(schedules);
    }

    /**
     * 获取重要日程
     */
    @GetMapping("/important")
    public Result<List<Schedule>> getImportantSchedules() {
        List<Schedule> schedules = scheduleService.getImportantSchedules();
        return Result.success(schedules);
    }

    /**
     * 完成日程
     */
    @PostMapping("/{id}/complete")
    public Result<Void> completeSchedule(@PathVariable Long id) {
        scheduleService.completeSchedule(id);
        return Result.success();
    }

    /**
     * 取消日程
     */
    @PostMapping("/{id}/cancel")
    public Result<Void> cancelSchedule(@PathVariable Long id) {
        scheduleService.cancelSchedule(id);
        return Result.success();
    }

    /**
     * 智能识别重要日期
     */
    @PostMapping("/identify")
    public Result<List<Schedule>> identifyImportantDates(@RequestParam String text) {
        List<Schedule> schedules = scheduleService.identifyImportantDates(text);
        return Result.success(schedules);
    }

    /**
     * 生成重复日程
     */
    @PostMapping("/{id}/generate")
    public Result<List<Schedule>> generateRepeatingSchedules(
            @PathVariable Long id, 
            @RequestParam String untilDate) {
        LocalDate until = LocalDate.parse(untilDate);
        List<Schedule> schedules = scheduleService.generateRepeatingSchedules(id, until);
        return Result.success(schedules);
    }

    /**
     * 获取日程统计信息
     */
    @GetMapping("/stats")
    public Result<Object> getScheduleStats() {
        Object stats = scheduleService.getScheduleStats();
        return Result.success(stats);
    }

    /**
     * 创建分级提醒
     */
    @PostMapping("/{id}/tiered-reminders")
    public Result<List<Reminder>> createTieredReminders(@PathVariable Long id) {
        Schedule schedule = scheduleService.getScheduleDetail(id);
        if (schedule == null) {
            return Result.error("日程不存在");
        }
        
        List<Reminder> reminders = reminderService.createTieredReminders(id, schedule.getPriority());
        return Result.success(reminders);
    }

    /**
     * 获取分级提醒配置
     */
    @GetMapping("/reminder-config")
    public Result<Object> getReminderConfig() {
        Object config = reminderService.getReminderPriorityConfig();
        return Result.success(config);
    }

    /**
     * 发送分级提醒
     */
    @PostMapping("/{id}/send-reminders")
    public Result<Void> sendTieredReminders(@PathVariable Long id) {
        reminderService.sendTieredReminders(id);
        return Result.success();
    }

    /**
     * 获取礼物推荐
     */
    @GetMapping("/gift-recommendations")
    public Result<List<GiftRecommendation>> getGiftRecommendations(
            @RequestParam(required = false) Integer targetAge,
            @RequestParam(required = false) Integer targetGender,
            @RequestParam(required = false) List<String> occasions,
            @RequestParam(required = false) BigDecimal maxBudget,
            @RequestParam(defaultValue = "10") Integer limit) {
        
        List<GiftRecommendation> recommendations = giftRecommendationService.getSmartRecommendations(
            targetAge, targetGender, occasions, maxBudget, limit);
        return Result.success(recommendations);
    }

    /**
     * 根据场合获取礼物推荐
     */
    @GetMapping("/gift-recommendations/occasion/{occasion}")
    public Result<List<GiftRecommendation>> getGiftRecommendationsByOccasion(
            @PathVariable String occasion,
            @RequestParam(defaultValue = "10") Integer limit) {
        
        List<GiftRecommendation> recommendations = giftRecommendationService.getRecommendationsByOccasion(occasion, limit);
        return Result.success(recommendations);
    }
}
