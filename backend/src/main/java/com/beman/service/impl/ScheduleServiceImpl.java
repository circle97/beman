package com.beman.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.mapper.ScheduleMapper;
import com.beman.model.Schedule;
import com.beman.model.dto.ReminderCreateDTO;
import com.beman.model.dto.ScheduleCreateDTO;
import com.beman.model.dto.ScheduleQueryDTO;
import com.beman.service.ImportantDateRecognitionService;
import com.beman.service.ReminderService;
import com.beman.service.ScheduleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * 日程服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleMapper scheduleMapper;
    private final ImportantDateRecognitionService importantDateRecognitionService;
    private final ReminderService reminderService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public Schedule createSchedule(ScheduleCreateDTO createDTO) {
        Long userId = StpUtil.getLoginIdAsLong();

        Schedule schedule = new Schedule();
        schedule.setUserId(userId);
        schedule.setTitle(createDTO.getTitle());
        schedule.setDescription(createDTO.getDescription());
        schedule.setType(createDTO.getType());
        schedule.setPriority(createDTO.getPriority());
        schedule.setStartTime(createDTO.getStartTime());
        schedule.setEndTime(createDTO.getEndTime());
        schedule.setIsAllDay(createDTO.getIsAllDay());
        schedule.setRepeatType(createDTO.getRepeatType());
        schedule.setRepeatEndTime(createDTO.getRepeatEndTime());
        schedule.setLocation(createDTO.getLocation());
        schedule.setStatus(1); // 正常状态

        // 处理标签
        if (createDTO.getTags() != null && !createDTO.getTags().isEmpty()) {
            try {
                schedule.setTags(objectMapper.writeValueAsString(createDTO.getTags()));
            } catch (JsonProcessingException e) {
                log.error("序列化标签失败", e);
            }
        }

        // 处理提醒设置
        if (createDTO.getReminderSettings() != null && !createDTO.getReminderSettings().isEmpty()) {
            try {
                schedule.setReminderSettings(objectMapper.writeValueAsString(createDTO.getReminderSettings()));
            } catch (JsonProcessingException e) {
                log.error("序列化提醒设置失败", e);
            }
        }

        scheduleMapper.insert(schedule);
        
        // 创建提醒
        if (createDTO.getReminderSettings() != null && !createDTO.getReminderSettings().isEmpty()) {
            try {
                // 将DTO中的提醒设置转换为提醒实体
                List<ReminderCreateDTO> reminderDTOs = new java.util.ArrayList<>();
                for (ScheduleCreateDTO.ReminderSetting setting : createDTO.getReminderSettings()) {
                    ReminderCreateDTO reminderDTO = new ReminderCreateDTO();
                    reminderDTO.setScheduleId(schedule.getId());
                    reminderDTO.setType(setting.getType());
                    reminderDTO.setAdvanceMinutes(setting.getAdvanceMinutes());
                    reminderDTO.setContent(setting.getContent());
                    
                    // 计算提醒时间
                    if (setting.getAdvanceMinutes() > 0) {
                        reminderDTO.setReminderTime(schedule.getStartTime().minusMinutes(setting.getAdvanceMinutes()));
                    } else {
                        reminderDTO.setReminderTime(schedule.getStartTime());
                    }
                    
                    reminderDTOs.add(reminderDTO);
                }
                
                reminderService.createRemindersFromSchedule(schedule.getId(), reminderDTOs);
            } catch (Exception e) {
                log.error("创建提醒失败", e);
            }
        }
        
        return schedule;
    }

    @Override
    @Transactional
    public Schedule updateSchedule(Long id, ScheduleCreateDTO updateDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        Schedule existingSchedule = scheduleMapper.selectById(id);
        
        if (existingSchedule == null || !Objects.equals(existingSchedule.getUserId(), userId)) {
            throw new RuntimeException("日程不存在或无权限");
        }

        existingSchedule.setTitle(updateDTO.getTitle());
        existingSchedule.setDescription(updateDTO.getDescription());
        existingSchedule.setType(updateDTO.getType());
        existingSchedule.setPriority(updateDTO.getPriority());
        existingSchedule.setStartTime(updateDTO.getStartTime());
        existingSchedule.setEndTime(updateDTO.getEndTime());
        existingSchedule.setIsAllDay(updateDTO.getIsAllDay());
        existingSchedule.setRepeatType(updateDTO.getRepeatType());
        existingSchedule.setRepeatEndTime(updateDTO.getRepeatEndTime());
        existingSchedule.setLocation(updateDTO.getLocation());

        // 处理标签
        if (updateDTO.getTags() != null) {
            try {
                existingSchedule.setTags(objectMapper.writeValueAsString(updateDTO.getTags()));
            } catch (JsonProcessingException e) {
                log.error("序列化标签失败", e);
            }
        }

        // 处理提醒设置
        if (updateDTO.getReminderSettings() != null) {
            try {
                existingSchedule.setReminderSettings(objectMapper.writeValueAsString(updateDTO.getReminderSettings()));
            } catch (JsonProcessingException e) {
                log.error("序列化提醒设置失败", e);
            }
        }

        scheduleMapper.updateById(existingSchedule);
        return existingSchedule;
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Schedule schedule = scheduleMapper.selectById(id);
        
        if (schedule == null || !Objects.equals(schedule.getUserId(), userId)) {
            throw new RuntimeException("日程不存在或无权限");
        }
        
        scheduleMapper.deleteById(id);
    }

    @Override
    public Schedule getScheduleDetail(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Schedule schedule = scheduleMapper.selectById(id);
        
        if (schedule == null || !Objects.equals(schedule.getUserId(), userId)) {
            throw new RuntimeException("日程不存在或无权限");
        }
        
        return schedule;
    }

    @Override
    public IPage<Schedule> getSchedulePage(ScheduleQueryDTO queryDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        Page<Schedule> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        
        return scheduleMapper.selectSchedulePage(page, userId, queryDTO.getKeyword(), 
            queryDTO.getType(), queryDTO.getPriority(), queryDTO.getStatus(),
            queryDTO.getStartTimeFrom(), queryDTO.getStartTimeTo(),
            queryDTO.getEndTimeFrom(), queryDTO.getEndTimeTo(),
            queryDTO.getDateFrom(), queryDTO.getDateTo(),
            queryDTO.getTags() != null ? String.join(",", queryDTO.getTags()) : null,
            queryDTO.getLocation(), queryDTO.getIncludeRepeating(),
            queryDTO.getSortBy(), queryDTO.getSortOrder());
    }

    @Override
    public List<Schedule> getSchedulesByDate(LocalDate date) {
        Long userId = StpUtil.getLoginIdAsLong();
        return scheduleMapper.selectSchedulesByDate(userId, date);
    }

    @Override
    public List<Schedule> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate) {
        Long userId = StpUtil.getLoginIdAsLong();
        return scheduleMapper.selectSchedulesByDateRange(userId, startDate, endDate);
    }

    @Override
    public List<Schedule> getUpcomingSchedules(int days) {
        Long userId = StpUtil.getLoginIdAsLong();
        return scheduleMapper.selectUpcomingSchedules(userId, days);
    }

    @Override
    public List<Schedule> getImportantSchedules() {
        Long userId = StpUtil.getLoginIdAsLong();
        return scheduleMapper.selectImportantSchedules(userId);
    }

    @Override
    @Transactional
    public void completeSchedule(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Schedule schedule = scheduleMapper.selectById(id);
        
        if (schedule == null || !Objects.equals(schedule.getUserId(), userId)) {
            throw new RuntimeException("日程不存在或无权限");
        }
        
        schedule.setStatus(2); // 已完成
        scheduleMapper.updateById(schedule);
    }

    @Override
    @Transactional
    public void cancelSchedule(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Schedule schedule = scheduleMapper.selectById(id);
        
        if (schedule == null || !Objects.equals(schedule.getUserId(), userId)) {
            throw new RuntimeException("日程不存在或无权限");
        }
        
        schedule.setStatus(3); // 已取消
        scheduleMapper.updateById(schedule);
    }

    @Override
    public List<Schedule> identifyImportantDates(String text) {
        return importantDateRecognitionService.identifyImportantDates(text);
    }

    @Override
    public List<Schedule> generateRepeatingSchedules(Long scheduleId, LocalDate untilDate) {
        // 这里简化实现，实际应该根据重复规则生成多个日程实例
        Long userId = StpUtil.getLoginIdAsLong();
        Schedule originalSchedule = scheduleMapper.selectById(scheduleId);
        
        if (originalSchedule == null || !Objects.equals(originalSchedule.getUserId(), userId)) {
            throw new RuntimeException("日程不存在或无权限");
        }
        
        // 简化实现：只返回原日程
        return java.util.Arrays.asList(originalSchedule);
    }

    @Override
    public ScheduleStats getScheduleStats() {
        Long userId = StpUtil.getLoginIdAsLong();
        
        return new ScheduleStats() {
            @Override
            public int getTodayCount() {
                return scheduleMapper.selectTodayCount(userId);
            }

            @Override
            public int getWeekCount() {
                return scheduleMapper.selectWeekCount(userId);
            }

            @Override
            public int getMonthCount() {
                return scheduleMapper.selectMonthCount(userId);
            }

            @Override
            public int getCompletedCount() {
                return scheduleMapper.selectCompletedCount(userId);
            }

            @Override
            public int getPendingCount() {
                return scheduleMapper.selectPendingCount(userId);
            }

            @Override
            public int getHighPriorityCount() {
                return scheduleMapper.selectHighPriorityCount(userId);
            }
        };
    }
}
