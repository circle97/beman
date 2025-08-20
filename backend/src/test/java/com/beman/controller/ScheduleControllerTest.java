package com.beman.controller;

import com.beman.model.Schedule;
import com.beman.model.Reminder;
import com.beman.model.GiftRecommendation;
import com.beman.model.dto.ScheduleCreateDTO;
import com.beman.service.ScheduleService;
import com.beman.service.ReminderService;
import com.beman.service.GiftRecommendationService;
import com.beman.model.vo.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 日程控制器单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("日程控制器测试")
class ScheduleControllerTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ReminderService reminderService;

    @Mock
    private GiftRecommendationService giftRecommendationService;

    @InjectMocks
    private ScheduleController scheduleController;

    private Schedule mockSchedule;
    private Reminder mockReminder;
    private GiftRecommendation mockGiftRecommendation;

    @BeforeEach
    void setUp() {
        // 创建模拟日程
        mockSchedule = new Schedule();
        mockSchedule.setId(1L);
        mockSchedule.setTitle("测试日程");
        mockSchedule.setDescription("这是一个测试日程");
        mockSchedule.setType(1);
        mockSchedule.setPriority(3);
        mockSchedule.setStartTime(LocalDateTime.now().plusDays(1));
        mockSchedule.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        mockSchedule.setIsAllDay(false);
        mockSchedule.setRepeatType(0);
        mockSchedule.setStatus(1);

        // 创建模拟提醒
        mockReminder = new Reminder();
        mockReminder.setId(1L);
        mockReminder.setScheduleId(1L);
        mockReminder.setType(3);
        mockReminder.setAdvanceMinutes(1440);
        mockReminder.setContent("您有一个高优先级日程即将开始");
        mockReminder.setStatus(1);

        // 创建模拟礼物推荐
        mockGiftRecommendation = new GiftRecommendation();
        mockGiftRecommendation.setId(1L);
        mockGiftRecommendation.setName("鲜花");
        mockGiftRecommendation.setDescription("美丽的玫瑰花束");
        mockGiftRecommendation.setMinPrice(new BigDecimal(50));
        mockGiftRecommendation.setMaxPrice(new BigDecimal(100));
        mockGiftRecommendation.setRating(4);
    }

    @Test
    @DisplayName("测试创建分级提醒")
    void testCreateTieredReminders() {
        // 准备测试数据
        Long scheduleId = 1L;
        when(scheduleService.getScheduleDetail(scheduleId)).thenReturn(mockSchedule);
        when(reminderService.createTieredReminders(scheduleId, mockSchedule.getPriority()))
            .thenReturn(Arrays.asList(mockReminder));

        // 执行测试
        Result<List<Reminder>> result = scheduleController.createTieredReminders(scheduleId);

        // 验证结果
        assertNotNull(result, "结果不应该为空");
        assertEquals(200, result.getCode(), "状态码应该是200");
        assertNotNull(result.getData(), "数据不应该为空");
        assertEquals(1, result.getData().size(), "应该返回1个提醒");

        // 验证服务调用
        verify(scheduleService).getScheduleDetail(scheduleId);
        verify(reminderService).createTieredReminders(scheduleId, mockSchedule.getPriority());
    }

    @Test
    @DisplayName("测试创建分级提醒 - 日程不存在")
    void testCreateTieredRemindersScheduleNotFound() {
        // 准备测试数据
        Long scheduleId = 999L;
        when(scheduleService.getScheduleDetail(scheduleId)).thenReturn(null);

        // 执行测试
        Result<List<Reminder>> result = scheduleController.createTieredReminders(scheduleId);

        // 验证结果
        assertNotNull(result, "结果不应该为空");
        assertEquals(500, result.getCode(), "状态码应该是500");
        assertEquals("日程不存在", result.getMessage(), "错误消息应该正确");

        // 验证服务调用
        verify(scheduleService).getScheduleDetail(scheduleId);
        verify(reminderService, never()).createTieredReminders(any(), any());
    }

    @Test
    @DisplayName("测试获取分级提醒配置")
    void testGetReminderConfig() {
        // 准备测试数据
        ReminderService.ReminderPriorityConfig mockConfig = mock(ReminderService.ReminderPriorityConfig.class);
        when(reminderService.getReminderPriorityConfig()).thenReturn(mockConfig);

        // 执行测试
        Result<Object> result = scheduleController.getReminderConfig();

        // 验证结果
        assertNotNull(result, "结果不应该为空");
        assertEquals(200, result.getCode(), "状态码应该是200");
        assertNotNull(result.getData(), "数据不应该为空");

        // 验证服务调用
        verify(reminderService).getReminderPriorityConfig();
    }

    @Test
    @DisplayName("测试发送分级提醒")
    void testSendTieredReminders() {
        // 准备测试数据
        Long scheduleId = 1L;
        doNothing().when(reminderService).sendTieredReminders(scheduleId);

        // 执行测试
        Result<Void> result = scheduleController.sendTieredReminders(scheduleId);

        // 验证结果
        assertNotNull(result, "结果不应该为空");
        assertEquals(200, result.getCode(), "状态码应该是200");

        // 验证服务调用
        verify(reminderService).sendTieredReminders(scheduleId);
    }

    @Test
    @DisplayName("测试获取礼物推荐")
    void testGetGiftRecommendations() {
        // 准备测试数据
        Integer targetAge = 25;
        Integer targetGender = 2;
        List<String> occasions = Arrays.asList("生日", "纪念日");
        BigDecimal maxBudget = new BigDecimal("200");
        Integer limit = 10;

        when(giftRecommendationService.getSmartRecommendations(targetAge, targetGender, occasions, maxBudget, limit))
            .thenReturn(Arrays.asList(mockGiftRecommendation));

        // 执行测试
        Result<List<GiftRecommendation>> result = scheduleController.getGiftRecommendations(
            targetAge, targetGender, occasions, maxBudget, limit);

        // 验证结果
        assertNotNull(result, "结果不应该为空");
        assertEquals(200, result.getCode(), "状态码应该是200");
        assertNotNull(result.getData(), "数据不应该为空");
        assertEquals(1, result.getData().size(), "应该返回1个礼物推荐");

        // 验证服务调用
        verify(giftRecommendationService).getSmartRecommendations(targetAge, targetGender, occasions, maxBudget, limit);
    }

    @Test
    @DisplayName("测试获取礼物推荐 - 无参数")
    void testGetGiftRecommendationsNoParams() {
        // 准备测试数据
        when(giftRecommendationService.getSmartRecommendations(null, null, null, null, 10))
            .thenReturn(Arrays.asList(mockGiftRecommendation));

        // 执行测试
        Result<List<GiftRecommendation>> result = scheduleController.getGiftRecommendations(
            null, null, null, null, 10);

        // 验证结果
        assertNotNull(result, "结果不应该为空");
        assertEquals(200, result.getCode(), "状态码应该是200");
        assertNotNull(result.getData(), "数据不应该为空");

        // 验证服务调用
        verify(giftRecommendationService).getSmartRecommendations(null, null, null, null, 10);
    }

    @Test
    @DisplayName("测试根据场合获取礼物推荐")
    void testGetGiftRecommendationsByOccasion() {
        // 准备测试数据
        String occasion = "生日";
        Integer limit = 5;
        when(giftRecommendationService.getRecommendationsByOccasion(occasion, limit))
            .thenReturn(Arrays.asList(mockGiftRecommendation));

        // 执行测试
        Result<List<GiftRecommendation>> result = scheduleController.getGiftRecommendationsByOccasion(occasion, limit);

        // 验证结果
        assertNotNull(result, "结果不应该为空");
        assertEquals(200, result.getCode(), "状态码应该是200");
        assertNotNull(result.getData(), "数据不应该为空");
        assertEquals(1, result.getData().size(), "应该返回1个礼物推荐");

        // 验证服务调用
        verify(giftRecommendationService).getRecommendationsByOccasion(occasion, limit);
    }

    @Test
    @DisplayName("测试根据场合获取礼物推荐 - 默认限制")
    void testGetGiftRecommendationsByOccasionDefaultLimit() {
        // 准备测试数据
        String occasion = "纪念日";
        when(giftRecommendationService.getRecommendationsByOccasion(occasion, 10))
            .thenReturn(Arrays.asList(mockGiftRecommendation));

        // 执行测试
        Result<List<GiftRecommendation>> result = scheduleController.getGiftRecommendationsByOccasion(occasion, null);

        // 验证结果
        assertNotNull(result, "结果不应该为空");
        assertEquals(200, result.getCode(), "状态码应该是200");

        // 验证服务调用，应该使用默认限制10
        verify(giftRecommendationService).getRecommendationsByOccasion(occasion, 10);
    }

    @Test
    @DisplayName("测试创建日程")
    void testCreateSchedule() {
        // 准备测试数据
        ScheduleCreateDTO createDTO = new ScheduleCreateDTO();
        createDTO.setTitle("新日程");
        createDTO.setDescription("新日程描述");
        createDTO.setType(1);
        createDTO.setPriority(2);
        createDTO.setStartTime(LocalDateTime.now().plusDays(1));
        createDTO.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        createDTO.setIsAllDay(false);
        createDTO.setRepeatType(0);

        when(scheduleService.createSchedule(createDTO)).thenReturn(mockSchedule);

        // 执行测试
        Result<Schedule> result = scheduleController.createSchedule(createDTO);

        // 验证结果
        assertNotNull(result, "结果不应该为空");
        assertEquals(200, result.getCode(), "状态码应该是200");
        assertNotNull(result.getData(), "数据不应该为空");
        assertEquals(mockSchedule, result.getData(), "返回的日程应该匹配");

        // 验证服务调用
        verify(scheduleService).createSchedule(createDTO);
    }

    @Test
    @DisplayName("测试获取日程详情")
    void testGetScheduleDetail() {
        // 准备测试数据
        Long scheduleId = 1L;
        when(scheduleService.getScheduleDetail(scheduleId)).thenReturn(mockSchedule);

        // 执行测试
        Result<Schedule> result = scheduleController.getScheduleDetail(scheduleId);

        // 验证结果
        assertNotNull(result, "结果不应该为空");
        assertEquals(200, result.getCode(), "状态码应该是200");
        assertNotNull(result.getData(), "数据不应该为空");
        assertEquals(mockSchedule, result.getData(), "返回的日程应该匹配");

        // 验证服务调用
        verify(scheduleService).getScheduleDetail(scheduleId);
    }

    @Test
    @DisplayName("测试完成日程")
    void testCompleteSchedule() {
        // 准备测试数据
        Long scheduleId = 1L;
        doNothing().when(scheduleService).completeSchedule(scheduleId);

        // 执行测试
        Result<Void> result = scheduleController.completeSchedule(scheduleId);

        // 验证结果
        assertNotNull(result, "结果不应该为空");
        assertEquals(200, result.getCode(), "状态码应该是200");

        // 验证服务调用
        verify(scheduleService).completeSchedule(scheduleId);
    }

    @Test
    @DisplayName("测试取消日程")
    void testCancelSchedule() {
        // 准备测试数据
        Long scheduleId = 1L;
        doNothing().when(scheduleService).cancelSchedule(scheduleId);

        // 执行测试
        Result<Void> result = scheduleController.cancelSchedule(scheduleId);

        // 验证结果
        assertNotNull(result, "结果不应该为空");
        assertEquals(200, result.getCode(), "状态码应该是200");

        // 验证服务调用
        verify(scheduleService).cancelSchedule(scheduleId);
    }
}
