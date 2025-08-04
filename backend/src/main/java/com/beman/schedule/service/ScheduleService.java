package com.beman.schedule.service;

import com.beman.common.BusinessException;
import com.beman.schedule.dto.ScheduleRequest;
import com.beman.schedule.dto.ScheduleResponse;
import com.beman.schedule.entity.Schedule;
import com.beman.schedule.entity.Schedule.ScheduleStatus;
import com.beman.schedule.repository.ScheduleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    
    private final ScheduleRepository scheduleRepository;
    private final ObjectMapper objectMapper;
    
    @Transactional
    public ScheduleResponse createSchedule(ScheduleRequest request, Long userId) {
        Schedule schedule = new Schedule();
        schedule.setUserId(userId);
        schedule.setTitle(request.getTitle());
        schedule.setDescription(request.getDescription());
        schedule.setEventDate(request.getEventDate());
        schedule.setEventType(request.getEventType());
        schedule.setGiftSuggestion(request.getGiftSuggestion());
        schedule.setRepeated(request.isRepeated());
        schedule.setRepeatType(request.getRepeatType());
        schedule.setStatus(ScheduleStatus.ACTIVE);
        
        // 处理提醒天数
        if (request.getReminderDays() != null && !request.getReminderDays().isEmpty()) {
            try {
                schedule.setReminderDays(objectMapper.writeValueAsString(request.getReminderDays()));
            } catch (JsonProcessingException e) {
                throw BusinessException.badRequest("提醒天数格式错误");
            }
        }
        
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return convertToResponse(savedSchedule);
    }
    
    public List<ScheduleResponse> getUserSchedules(Long userId) {
        List<Schedule> schedules = scheduleRepository.findByUserIdAndStatusOrderByEventDateAsc(userId, ScheduleStatus.ACTIVE);
        return schedules.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
    
    public Page<ScheduleResponse> getUserSchedules(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Schedule> schedules = scheduleRepository.findByUserIdAndStatusOrderByEventDateAsc(userId, ScheduleStatus.ACTIVE, pageable);
        return schedules.map(this::convertToResponse);
    }
    
    public List<ScheduleResponse> getUpcomingSchedules(Long userId) {
        LocalDate startDate = LocalDate.now();
        List<Schedule> schedules = scheduleRepository.findUpcomingSchedules(userId, ScheduleStatus.ACTIVE, startDate);
        return schedules.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
    
    public ScheduleResponse getScheduleById(Long scheduleId, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> BusinessException.notFound("日程不存在"));
        
        if (!schedule.getUserId().equals(userId)) {
            throw BusinessException.forbidden("无权限访问此日程");
        }
        
        return convertToResponse(schedule);
    }
    
    @Transactional
    public ScheduleResponse updateSchedule(Long scheduleId, ScheduleRequest request, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> BusinessException.notFound("日程不存在"));
        
        if (!schedule.getUserId().equals(userId)) {
            throw BusinessException.forbidden("无权限修改此日程");
        }
        
        schedule.setTitle(request.getTitle());
        schedule.setDescription(request.getDescription());
        schedule.setEventDate(request.getEventDate());
        schedule.setEventType(request.getEventType());
        schedule.setGiftSuggestion(request.getGiftSuggestion());
        schedule.setRepeated(request.isRepeated());
        schedule.setRepeatType(request.getRepeatType());
        
        // 处理提醒天数
        if (request.getReminderDays() != null && !request.getReminderDays().isEmpty()) {
            try {
                schedule.setReminderDays(objectMapper.writeValueAsString(request.getReminderDays()));
            } catch (JsonProcessingException e) {
                throw BusinessException.badRequest("提醒天数格式错误");
            }
        }
        
        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return convertToResponse(updatedSchedule);
    }
    
    @Transactional
    public void deleteSchedule(Long scheduleId, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> BusinessException.notFound("日程不存在"));
        
        if (!schedule.getUserId().equals(userId)) {
            throw BusinessException.forbidden("无权限删除此日程");
        }
        
        scheduleRepository.delete(schedule);
    }
    
    @Transactional
    public void completeSchedule(Long scheduleId, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> BusinessException.notFound("日程不存在"));
        
        if (!schedule.getUserId().equals(userId)) {
            throw BusinessException.forbidden("无权限操作此日程");
        }
        
        schedule.setStatus(ScheduleStatus.COMPLETED);
        scheduleRepository.save(schedule);
    }
    
    private ScheduleResponse convertToResponse(Schedule schedule) {
        ScheduleResponse response = new ScheduleResponse();
        response.setId(schedule.getId());
        response.setTitle(schedule.getTitle());
        response.setDescription(schedule.getDescription());
        response.setEventDate(schedule.getEventDate());
        response.setEventType(schedule.getEventType());
        response.setGiftSuggestion(schedule.getGiftSuggestion());
        response.setRepeated(schedule.isRepeated());
        response.setRepeatType(schedule.getRepeatType());
        response.setStatus(schedule.getStatus());
        response.setCreatedAt(schedule.getCreatedAt());
        response.setUpdatedAt(schedule.getUpdatedAt());
        
        // 计算距离事件的天数
        LocalDate today = LocalDate.now();
        long daysUntilEvent = ChronoUnit.DAYS.between(today, schedule.getEventDate());
        response.setDaysUntilEvent(daysUntilEvent);
        response.setUpcoming(daysUntilEvent >= 0 && daysUntilEvent <= 30);
        
        // 处理提醒天数
        if (schedule.getReminderDays() != null && !schedule.getReminderDays().isEmpty()) {
            try {
                List<Integer> reminderDays = objectMapper.readValue(schedule.getReminderDays(), new TypeReference<List<Integer>>() {});
                response.setReminderDays(reminderDays);
            } catch (JsonProcessingException e) {
                response.setReminderDays(java.util.Collections.emptyList());
            }
        }
        
        return response;
    }
} 