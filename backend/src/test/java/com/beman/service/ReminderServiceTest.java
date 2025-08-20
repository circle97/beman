package com.beman.service;

import com.beman.model.Reminder;
import com.beman.service.impl.ReminderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * 提醒服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("提醒服务测试")
class ReminderServiceTest {

    @Mock
    private com.beman.mapper.ReminderMapper reminderMapper;

    private ReminderService reminderService;

    @BeforeEach
    void setUp() {
        reminderService = new ReminderServiceImpl(reminderMapper);
    }

    @Test
    @DisplayName("测试创建分级提醒 - 低优先级")
    void testCreateTieredRemindersLowPriority() {
        Long scheduleId = 1L;
        Integer priority = 1; // 低优先级
        
        List<Reminder> reminders = reminderService.createTieredReminders(scheduleId, priority);
        
        assertNotNull(reminders, "应该返回提醒列表");
        assertEquals(1, reminders.size(), "低优先级应该创建1个提醒");
        
        Reminder reminder = reminders.get(0);
        assertEquals(scheduleId, reminder.getScheduleId(), "日程ID应该匹配");
        assertEquals(1, reminder.getStatus(), "状态应该是待发送");
        assertEquals(0, reminder.getRetryCount(), "重试次数应该是0");
        assertEquals(3, reminder.getMaxRetryCount(), "最大重试次数应该是3");
    }

    @Test
    @DisplayName("测试获取分级提醒配置 - 低优先级")
    void testGetTieredReminderConfigsLowPriority() {
        Integer priority = 1;
        List<ReminderService.TieredReminderConfig> configs = reminderService.getTieredReminderConfigs(priority);
        
        assertNotNull(configs, "应该返回配置列表");
        assertEquals(1, configs.size(), "低优先级应该有1个配置");
        
        ReminderService.TieredReminderConfig config = configs.get(0);
        assertEquals(1, config.getLevel(), "级别应该是1");
        assertEquals(1440, config.getAdvanceMinutes(), "提前时间应该是1440分钟");
        assertEquals(3, config.getType(), "提醒类型应该是推送");
        assertNotNull(config.getContentTemplate(), "内容模板不应该为空");
    }

    @Test
    @DisplayName("测试获取分级提醒配置 - 中优先级")
    void testGetTieredReminderConfigsMediumPriority() {
        Integer priority = 2;
        List<ReminderService.TieredReminderConfig> configs = reminderService.getTieredReminderConfigs(priority);
        
        assertNotNull(configs, "应该返回配置列表");
        assertEquals(2, configs.size(), "中优先级应该有2个配置");
        
        // 检查第一个配置
        ReminderService.TieredReminderConfig firstConfig = configs.get(0);
        assertEquals(1, firstConfig.getLevel(), "第一个级别应该是1");
        assertEquals(1440, firstConfig.getAdvanceMinutes(), "第一个提前时间应该是1440分钟");
        
        // 检查第二个配置
        ReminderService.TieredReminderConfig secondConfig = configs.get(1);
        assertEquals(2, secondConfig.getLevel(), "第二个级别应该是2");
        assertEquals(60, secondConfig.getAdvanceMinutes(), "第二个提前时间应该是60分钟");
    }

    @Test
    @DisplayName("测试获取分级提醒配置 - 高优先级")
    void testGetTieredReminderConfigsHighPriority() {
        Integer priority = 3;
        List<ReminderService.TieredReminderConfig> configs = reminderService.getTieredReminderConfigs(priority);
        
        assertNotNull(configs, "应该返回配置列表");
        assertEquals(3, configs.size(), "高优先级应该有3个配置");
        
        // 检查配置级别
        int[] expectedLevels = {1, 2, 3};
        for (int i = 0; i < configs.size(); i++) {
            assertEquals(expectedLevels[i], configs.get(i).getLevel(), 
                "第" + (i+1) + "个配置的级别应该正确");
        }
    }

    @Test
    @DisplayName("测试获取分级提醒配置 - 紧急优先级")
    void testGetTieredReminderConfigsUrgentPriority() {
        Integer priority = 4;
        List<ReminderService.TieredReminderConfig> configs = reminderService.getTieredReminderConfigs(priority);
        
        assertNotNull(configs, "应该返回配置列表");
        assertEquals(4, configs.size(), "紧急优先级应该有4个配置");
        
        // 检查配置级别
        int[] expectedLevels = {1, 2, 3, 3};
        for (int i = 0; i < configs.size(); i++) {
            assertEquals(expectedLevels[i], configs.get(i).getLevel(), 
                "第" + (i+1) + "个配置的级别应该正确");
        }
    }

    @Test
    @DisplayName("测试获取分级提醒配置 - 无效优先级")
    void testGetTieredReminderConfigsInvalidPriority() {
        Integer priority = 99; // 无效优先级
        List<ReminderService.TieredReminderConfig> configs = reminderService.getTieredReminderConfigs(priority);
        
        assertNotNull(configs, "应该返回空列表");
        assertTrue(configs.isEmpty(), "无效优先级应该返回空配置");
    }

    @Test
    @DisplayName("测试获取提醒优先级配置")
    void testGetReminderPriorityConfig() {
        ReminderService.ReminderPriorityConfig config = reminderService.getReminderPriorityConfig();
        
        assertNotNull(config, "应该返回优先级配置");
        
        // 检查各个优先级的配置
        assertNotNull(config.getLowPriorityConfig(), "低优先级配置不应该为空");
        assertNotNull(config.getMediumPriorityConfig(), "中优先级配置不应该为空");
        assertNotNull(config.getHighPriorityConfig(), "高优先级配置不应该为空");
        assertNotNull(config.getUrgentPriorityConfig(), "紧急优先级配置不应该为空");
        
        // 检查配置数量
        assertEquals(1, config.getLowPriorityConfig().size(), "低优先级应该有1个配置");
        assertEquals(2, config.getMediumPriorityConfig().size(), "中优先级应该有2个配置");
        assertEquals(3, config.getHighPriorityConfig().size(), "高优先级应该有3个配置");
        assertEquals(4, config.getUrgentPriorityConfig().size(), "紧急优先级应该有4个配置");
    }

    @Test
    @DisplayName("测试提醒内容模板 - 低优先级")
    void testReminderContentTemplateLowPriority() {
        Integer priority = 1;
        List<ReminderService.TieredReminderConfig> configs = reminderService.getTieredReminderConfigs(priority);
        
        ReminderService.TieredReminderConfig config = configs.get(0);
        String content = config.getContentTemplate();
        
        assertNotNull(content, "内容模板不应该为空");
        assertTrue(content.contains("低优先级"), "内容应该包含优先级信息");
        assertTrue(content.contains("日程即将开始"), "内容应该包含日程信息");
    }

    @Test
    @DisplayName("测试提醒内容模板 - 紧急优先级")
    void testReminderContentTemplateUrgentPriority() {
        Integer priority = 4;
        List<ReminderService.TieredReminderConfig> configs = reminderService.getTieredReminderConfigs(priority);
        
        // 检查最后一个提醒（5分钟前）
        ReminderService.TieredReminderConfig lastConfig = configs.get(configs.size() - 1);
        String content = lastConfig.getContentTemplate();
        
        assertNotNull(content, "内容模板不应该为空");
        assertTrue(content.contains("紧急"), "内容应该包含紧急信息");
        assertTrue(content.contains("立即处理"), "内容应该包含立即处理信息");
    }
}
