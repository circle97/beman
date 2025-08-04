package com.beman.schedule.controller;

import com.beman.common.ApiResponse;
import com.beman.schedule.dto.ScheduleRequest;
import com.beman.schedule.dto.ScheduleResponse;
import com.beman.schedule.service.ScheduleService;
import com.beman.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    
    private final ScheduleService scheduleService;
    
    @PostMapping
    public ApiResponse<ScheduleResponse> createSchedule(@Valid @RequestBody ScheduleRequest request, @CurrentUser Long userId) {
        ScheduleResponse response = scheduleService.createSchedule(request, userId);
        return ApiResponse.success(response);
    }
    
    @GetMapping
    public ApiResponse<List<ScheduleResponse>> getUserSchedules(@CurrentUser Long userId) {
        List<ScheduleResponse> schedules = scheduleService.getUserSchedules(userId);
        return ApiResponse.success(schedules);
    }
    
    @GetMapping("/page")
    public ApiResponse<Page<ScheduleResponse>> getUserSchedulesPage(
            @CurrentUser Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ScheduleResponse> schedules = scheduleService.getUserSchedules(userId, page, size);
        return ApiResponse.success(schedules);
    }
    
    @GetMapping("/upcoming")
    public ApiResponse<List<ScheduleResponse>> getUpcomingSchedules(@CurrentUser Long userId) {
        List<ScheduleResponse> schedules = scheduleService.getUpcomingSchedules(userId);
        return ApiResponse.success(schedules);
    }
    
    @GetMapping("/{scheduleId}")
    public ApiResponse<ScheduleResponse> getScheduleById(@PathVariable Long scheduleId, @CurrentUser Long userId) {
        ScheduleResponse schedule = scheduleService.getScheduleById(scheduleId, userId);
        return ApiResponse.success(schedule);
    }
    
    @PutMapping("/{scheduleId}")
    public ApiResponse<ScheduleResponse> updateSchedule(
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleRequest request,
            @CurrentUser Long userId) {
        ScheduleResponse response = scheduleService.updateSchedule(scheduleId, request, userId);
        return ApiResponse.success(response);
    }
    
    @DeleteMapping("/{scheduleId}")
    public ApiResponse<Void> deleteSchedule(@PathVariable Long scheduleId, @CurrentUser Long userId) {
        scheduleService.deleteSchedule(scheduleId, userId);
        return ApiResponse.success(null);
    }
    
    @PostMapping("/{scheduleId}/complete")
    public ApiResponse<Void> completeSchedule(@PathVariable Long scheduleId, @CurrentUser Long userId) {
        scheduleService.completeSchedule(scheduleId, userId);
        return ApiResponse.success(null);
    }
} 