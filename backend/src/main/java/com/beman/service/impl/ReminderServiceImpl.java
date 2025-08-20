package com.beman.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beman.mapper.ReminderMapper;
import com.beman.model.Reminder;
import com.beman.model.dto.ReminderCreateDTO;
import com.beman.model.dto.ReminderQueryDTO;
import com.beman.service.ReminderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 提醒服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final ReminderMapper reminderMapper;

    @Override
    @Transactional
    public Reminder createReminder(ReminderCreateDTO createDTO) {
        Long userId = StpUtil.getLoginIdAsLong();

        Reminder reminder = new Reminder();
        reminder.setScheduleId(createDTO.getScheduleId());
        reminder.setUserId(userId);
        reminder.setType(createDTO.getType());
        reminder.setReminderTime(createDTO.getReminderTime());
        reminder.setAdvanceMinutes(createDTO.getAdvanceMinutes());
        reminder.setContent(createDTO.getContent());
        reminder.setStatus(1); // 待发送
        reminder.setRetryCount(0);
        reminder.setMaxRetryCount(createDTO.getMaxRetryCount());

        reminderMapper.insert(reminder);
        return reminder;
    }

    @Override
    @Transactional
    public Reminder updateReminder(Long id, ReminderCreateDTO updateDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        Reminder existingReminder = reminderMapper.selectById(id);
        
        if (existingReminder == null || !Objects.equals(existingReminder.getUserId(), userId)) {
            throw new RuntimeException("提醒不存在或无权限");
        }

        existingReminder.setType(updateDTO.getType());
        existingReminder.setReminderTime(updateDTO.getReminderTime());
        existingReminder.setAdvanceMinutes(updateDTO.getAdvanceMinutes());
        existingReminder.setContent(updateDTO.getContent());
        existingReminder.setMaxRetryCount(updateDTO.getMaxRetryCount());

        reminderMapper.updateById(existingReminder);
        return existingReminder;
    }

    @Override
    @Transactional
    public void deleteReminder(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Reminder reminder = reminderMapper.selectById(id);
        
        if (reminder == null || !Objects.equals(reminder.getUserId(), userId)) {
            throw new RuntimeException("提醒不存在或无权限");
        }
        
        reminderMapper.deleteById(id);
    }

    @Override
    public Reminder getReminderDetail(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Reminder reminder = reminderMapper.selectById(id);
        
        if (reminder == null || !Objects.equals(reminder.getUserId(), userId)) {
            throw new RuntimeException("提醒不存在或无权限");
        }
        
        return reminder;
    }

    @Override
    public List<Reminder> getReminderList(ReminderQueryDTO queryDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        QueryWrapper<Reminder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        
        if (queryDTO.getScheduleId() != null) {
            queryWrapper.eq("schedule_id", queryDTO.getScheduleId());
        }
        
        if (queryDTO.getType() != null) {
            queryWrapper.eq("type", queryDTO.getType());
        }
        
        if (queryDTO.getStatus() != null) {
            queryWrapper.eq("status", queryDTO.getStatus());
        }
        
        if (queryDTO.getReminderTimeFrom() != null) {
            queryWrapper.ge("reminder_time", queryDTO.getReminderTimeFrom());
        }
        
        if (queryDTO.getReminderTimeTo() != null) {
            queryWrapper.le("reminder_time", queryDTO.getReminderTimeTo());
        }
        
        if (!queryDTO.getIncludeCancelled()) {
            queryWrapper.ne("status", 4); // 不包含已取消的
        }
        
        queryWrapper.orderByDesc("reminder_time");
        
        return reminderMapper.selectList(queryWrapper);
    }

    @Override
    public List<Reminder> getPendingReminders() {
        return reminderMapper.selectPendingReminders();
    }

    @Override
    public List<Reminder> getUserReminders(Long userId) {
        return reminderMapper.selectUserReminders(userId);
    }

    @Override
    @Transactional
    public void markAsSent(Long id) {
        Reminder reminder = reminderMapper.selectById(id);
        if (reminder != null) {
            reminder.setStatus(2); // 已发送
            reminder.setSendTime(LocalDateTime.now());
            reminderMapper.updateById(reminder);
        }
    }

    @Override
    @Transactional
    public void markAsFailed(Long id, String reason) {
        Reminder reminder = reminderMapper.selectById(id);
        if (reminder != null) {
            reminder.setStatus(3); // 发送失败
            reminder.setFailReason(reason);
            reminder.setSendTime(LocalDateTime.now());
            reminderMapper.updateById(reminder);
        }
    }

    @Override
    @Transactional
    public void retryReminder(Long id) {
        Reminder reminder = reminderMapper.selectById(id);
        if (reminder != null && reminder.getRetryCount() < reminder.getMaxRetryCount()) {
            reminder.setRetryCount(reminder.getRetryCount() + 1);
            reminder.setStatus(1); // 重新设置为待发送
            reminder.setFailReason(null);
            reminderMapper.updateById(reminder);
        }
    }

    @Override
    @Transactional
    public void cancelReminder(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Reminder reminder = reminderMapper.selectById(id);
        
        if (reminder == null || !Objects.equals(reminder.getUserId(), userId)) {
            throw new RuntimeException("提醒不存在或无权限");
        }
        
        reminder.setStatus(4); // 已取消
        reminderMapper.updateById(reminder);
    }

    @Override
    @Transactional
    public List<Reminder> createRemindersFromSchedule(Long scheduleId, List<ReminderCreateDTO> reminderSettings) {
        List<Reminder> reminders = new java.util.ArrayList<>();
        
        if (reminderSettings != null && !reminderSettings.isEmpty()) {
            for (ReminderCreateDTO setting : reminderSettings) {
                Reminder reminder = new Reminder();
                reminder.setScheduleId(scheduleId);
                reminder.setUserId(StpUtil.getLoginIdAsLong());
                reminder.setType(setting.getType());
                reminder.setReminderTime(setting.getReminderTime());
                reminder.setAdvanceMinutes(setting.getAdvanceMinutes());
                reminder.setContent(setting.getContent());
                reminder.setStatus(1); // 待发送
                reminder.setRetryCount(0);
                reminder.setMaxRetryCount(setting.getMaxRetryCount());
                
                reminderMapper.insert(reminder);
                reminders.add(reminder);
            }
        }
        
        return reminders;
    }

    @Override
    public List<Reminder> getUpcomingReminders(int minutes) {
        return reminderMapper.selectUpcomingReminders(minutes);
    }

    @Override
    public void processReminders() {
        try {
            List<Reminder> pendingReminders = getPendingReminders();
            LocalDateTime now = LocalDateTime.now();
            
            for (Reminder reminder : pendingReminders) {
                if (reminder.getReminderTime().isBefore(now) || reminder.getReminderTime().isEqual(now)) {
                    // 发送提醒
                    sendReminder(reminder);
                }
            }
        } catch (Exception e) {
            log.error("处理提醒时发生错误", e);
        }
    }

    /**
     * 发送提醒（这里简化实现，实际应该调用具体的发送服务）
     */
    private void sendReminder(Reminder reminder) {
        try {
            log.info("发送提醒: {}", reminder.getContent());
            
            // 这里应该调用具体的发送服务
            // 比如：邮件服务、短信服务、推送服务等
            
            // 模拟发送成功
            markAsSent(reminder.getId());
            
        } catch (Exception e) {
            log.error("发送提醒失败", e);
            markAsFailed(reminder.getId(), e.getMessage());
        }
    }

    @Override
    public ReminderStats getReminderStats() {
        return new ReminderStats() {
            @Override
            public int getPendingCount() {
                return reminderMapper.selectPendingCount();
            }

            @Override
            public int getSentCount() {
                return reminderMapper.selectSentCount();
            }

            @Override
            public int getFailedCount() {
                return reminderMapper.selectFailedCount();
            }

            @Override
            public int getTodaySentCount() {
                return reminderMapper.selectTodaySentCount();
            }

            @Override
            public int getWeekSentCount() {
                return reminderMapper.selectWeekSentCount();
            }
        };
    }

    @Override
    @Transactional
    public List<Reminder> createTieredReminders(Long scheduleId, Integer priority) {
        List<Reminder> reminders = new java.util.ArrayList<>();
        List<TieredReminderConfig> configs = getTieredReminderConfigs(priority);
        
        for (TieredReminderConfig config : configs) {
            Reminder reminder = new Reminder();
            reminder.setScheduleId(scheduleId);
            reminder.setUserId(StpUtil.getLoginIdAsLong());
            reminder.setType(config.getType());
            reminder.setAdvanceMinutes(config.getAdvanceMinutes());
            reminder.setContent(config.getContentTemplate());
            reminder.setStatus(1); // 待发送
            reminder.setRetryCount(0);
            reminder.setMaxRetryCount(3);
            
            // 计算提醒时间（这里需要从日程获取开始时间）
            // 简化实现，实际应该查询日程的开始时间
            reminder.setReminderTime(LocalDateTime.now().plusMinutes(config.getAdvanceMinutes()));
            
            reminderMapper.insert(reminder);
            reminders.add(reminder);
        }
        
        return reminders;
    }

    @Override
    public List<TieredReminderConfig> getTieredReminderConfigs(Integer priority) {
        List<TieredReminderConfig> configs = new java.util.ArrayList<>();
        
        switch (priority) {
            case 1: // 低优先级
                configs.add(new TieredReminderConfig() {
                    @Override public int getLevel() { return 1; }
                    @Override public int getAdvanceMinutes() { return 1440; } // 1天前
                    @Override public int getType() { return 3; } // 推送
                    @Override public String getContentTemplate() { return "您有一个低优先级日程即将开始"; }
                });
                break;
                
            case 2: // 中优先级
                configs.add(new TieredReminderConfig() {
                    @Override public int getLevel() { return 1; }
                    @Override public int getAdvanceMinutes() { return 1440; } // 1天前
                    @Override public int getType() { return 3; } // 推送
                    @Override public String getContentTemplate() { return "您有一个中优先级日程即将开始"; }
                });
                configs.add(new TieredReminderConfig() {
                    @Override public int getLevel() { return 2; }
                    @Override public int getAdvanceMinutes() { return 60; } // 1小时前
                    @Override public int getType() { return 3; } // 推送
                    @Override public String getContentTemplate() { return "您有一个中优先级日程即将开始，请做好准备"; }
                });
                break;
                
            case 3: // 高优先级
                configs.add(new TieredReminderConfig() {
                    @Override public int getLevel() { return 1; }
                    @Override public int getAdvanceMinutes() { return 1440; } // 1天前
                    @Override public int getType() { return 3; } // 推送
                    @Override public String getContentTemplate() { return "您有一个高优先级日程即将开始"; }
                });
                configs.add(new TieredReminderConfig() {
                    @Override public int getLevel() { return 2; }
                    @Override public int getAdvanceMinutes() { return 120; } // 2小时前
                    @Override public int getType() { return 3; } // 推送
                    @Override public String getContentTemplate() { return "您有一个高优先级日程即将开始，请做好准备"; }
                });
                configs.add(new TieredReminderConfig() {
                    @Override public int getLevel() { return 3; }
                    @Override public int getAdvanceMinutes() { return 30; } // 30分钟前
                    @Override public int getType() { return 3; } // 推送
                    @Override public String getContentTemplate() { return "紧急提醒：您有一个高优先级日程即将开始！"; }
                });
                break;
                
            case 4: // 紧急优先级
                configs.add(new TieredReminderConfig() {
                    @Override public int getLevel() { return 1; }
                    @Override public int getAdvanceMinutes() { return 1440; } // 1天前
                    @Override public int getType() { return 3; } // 推送
                    @Override public String getContentTemplate() { return "您有一个紧急日程即将开始"; }
                });
                configs.add(new TieredReminderConfig() {
                    @Override public int getLevel() { return 2; }
                    @Override public int getAdvanceMinutes() { return 120; } // 2小时前
                    @Override public int getType() { return 3; } // 推送
                    @Override public String getContentTemplate() { return "您有一个紧急日程即将开始，请立即准备！"; }
                });
                configs.add(new TieredReminderConfig() {
                    @Override public int getLevel() { return 3; }
                    @Override public int getAdvanceMinutes() { return 30; } // 30分钟前
                    @Override public int getType() { return 3; } // 推送
                    @Override public String getContentTemplate() { return "紧急提醒：您有一个紧急日程即将开始！请立即处理！"; }
                });
                configs.add(new TieredReminderConfig() {
                    @Override public int getLevel() { return 3; }
                    @Override public int getAdvanceMinutes() { return 5; } // 5分钟前
                    @Override public int getType() { return 3; } // 推送
                    @Override public String getContentTemplate() { return "最后提醒：您有一个紧急日程即将开始！立即处理！"; }
                });
                break;
        }
        
        return configs;
    }

    @Override
    public void sendTieredReminders(Long scheduleId) {
        try {
            List<Reminder> reminders = reminderMapper.selectByScheduleId(scheduleId);
            LocalDateTime now = LocalDateTime.now();
            
            for (Reminder reminder : reminders) {
                if (reminder.getStatus() == 1 && 
                    (reminder.getReminderTime().isBefore(now) || reminder.getReminderTime().isEqual(now))) {
                    sendReminder(reminder);
                }
            }
        } catch (Exception e) {
            log.error("发送分级提醒时发生错误", e);
        }
    }

    @Override
    public ReminderPriorityConfig getReminderPriorityConfig() {
        return new ReminderPriorityConfig() {
            @Override
            public List<TieredReminderConfig> getLowPriorityConfig() {
                return getTieredReminderConfigs(1);
            }

            @Override
            public List<TieredReminderConfig> getMediumPriorityConfig() {
                return getTieredReminderConfigs(2);
            }

            @Override
            public List<TieredReminderConfig> getHighPriorityConfig() {
                return getTieredReminderConfigs(3);
            }

            @Override
            public List<TieredReminderConfig> getUrgentPriorityConfig() {
                return getTieredReminderConfigs(4);
            }
        };
    }
}
