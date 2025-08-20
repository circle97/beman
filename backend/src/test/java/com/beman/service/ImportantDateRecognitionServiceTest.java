package com.beman.service;

import com.beman.model.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 重要日期识别服务单元测试
 */
@DisplayName("重要日期识别服务测试")
class ImportantDateRecognitionServiceTest {

    private ImportantDateRecognitionService importantDateRecognitionService;

    @BeforeEach
    void setUp() {
        importantDateRecognitionService = new ImportantDateRecognitionService();
    }

    @Test
    @DisplayName("测试生日识别 - 中文关键词")
    void testBirthdayRecognitionChinese() {
        String text = "明天是我女朋友的生日，我们约好一起庆祝";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        assertNotNull(schedules, "应该返回日程列表");
        assertFalse(schedules.isEmpty(), "应该识别到生日日程");
        
        Schedule schedule = schedules.get(0);
        assertEquals("生日提醒", schedule.getTitle(), "标题应该是生日提醒");
        assertEquals(1, schedule.getType(), "类型应该是生日");
        assertEquals(3, schedule.getPriority(), "优先级应该是高");
        assertTrue(schedule.getIsAllDay(), "生日应该是全天");
        assertEquals(4, schedule.getRepeatType(), "生日应该每年重复");
    }

    @Test
    @DisplayName("测试生日识别 - 英文关键词")
    void testBirthdayRecognitionEnglish() {
        String text = "Tomorrow is my girlfriend's birthday, we plan to celebrate together";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        assertNotNull(schedules, "应该返回日程列表");
        assertFalse(schedules.isEmpty(), "应该识别到生日日程");
        
        Schedule schedule = schedules.get(0);
        assertEquals("生日提醒", schedule.getTitle(), "标题应该是生日提醒");
        assertEquals(1, schedule.getType(), "类型应该是生日");
    }

    @Test
    @DisplayName("测试纪念日识别")
    void testAnniversaryRecognition() {
        String text = "下个月是我们结婚一周年纪念日";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        assertNotNull(schedules, "应该返回日程列表");
        assertFalse(schedules.isEmpty(), "应该识别到纪念日日程");
        
        Schedule schedule = schedules.get(0);
        assertEquals("纪念日提醒", schedule.getTitle(), "标题应该是纪念日提醒");
        assertEquals(2, schedule.getType(), "类型应该是纪念日");
        assertEquals(3, schedule.getPriority(), "优先级应该是高");
        assertTrue(schedule.getIsAllDay(), "纪念日应该是全天");
        assertEquals(4, schedule.getRepeatType(), "纪念日应该每年重复");
    }

    @Test
    @DisplayName("测试节日识别 - 春节")
    void testFestivalRecognitionSpringFestival() {
        String text = "春节快到了，要准备年货";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        assertNotNull(schedules, "应该返回日程列表");
        assertFalse(schedules.isEmpty(), "应该识别到节日日程");
        
        Schedule schedule = schedules.get(0);
        assertEquals("春节提醒", schedule.getTitle(), "标题应该是春节提醒");
        assertEquals(3, schedule.getType(), "类型应该是节日");
        assertEquals(2, schedule.getPriority(), "优先级应该是中");
        assertTrue(schedule.getIsAllDay(), "节日应该是全天");
        assertEquals(4, schedule.getRepeatType(), "节日应该每年重复");
    }

    @Test
    @DisplayName("测试节日识别 - 中秋")
    void testFestivalRecognitionMidAutumn() {
        String text = "中秋节要到了，准备买月饼";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        assertNotNull(schedules, "应该返回日程列表");
        assertFalse(schedules.isEmpty(), "应该识别到节日日程");
        
        Schedule schedule = schedules.get(0);
        assertEquals("中秋提醒", schedule.getTitle(), "标题应该是中秋提醒");
        assertEquals(3, schedule.getType(), "类型应该是节日");
    }

    @Test
    @DisplayName("测试约会识别")
    void testDateRecognition() {
        String text = "明天晚上7点一起看电影，约会";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        assertNotNull(schedules, "应该返回日程列表");
        assertFalse(schedules.isEmpty(), "应该识别到约会日程");
        
        Schedule schedule = schedules.get(0);
        assertEquals("约会提醒", schedule.getTitle(), "标题应该是约会提醒");
        assertEquals(4, schedule.getType(), "类型应该是约会");
        assertEquals(2, schedule.getPriority(), "优先级应该是中");
        assertFalse(schedule.getIsAllDay(), "约会不应该是全天");
        assertEquals(0, schedule.getRepeatType(), "约会不应该重复");
    }

    @Test
    @DisplayName("测试会议识别")
    void testMeetingRecognition() {
        String text = "明天上午9点开项目讨论会";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        assertNotNull(schedules, "应该返回日程列表");
        assertFalse(schedules.isEmpty(), "应该识别到会议日程");
        
        Schedule schedule = schedules.get(0);
        assertEquals("会议提醒", schedule.getTitle(), "标题应该是会议提醒");
        assertEquals(5, schedule.getType(), "类型应该是会议");
        assertEquals(3, schedule.getPriority(), "优先级应该是高");
        assertFalse(schedule.getIsAllDay(), "会议不应该是全天");
        assertEquals(0, schedule.getRepeatType(), "会议不应该重复");
    }

    @Test
    @DisplayName("测试多种类型同时识别")
    void testMultipleTypeRecognition() {
        String text = "明天是我女朋友的生日，我们约好晚上7点一起吃饭，然后看电影";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        assertNotNull(schedules, "应该返回日程列表");
        assertTrue(schedules.size() >= 2, "应该识别到多个日程");
        
        // 检查是否包含生日和约会
        boolean hasBirthday = schedules.stream().anyMatch(s -> s.getType() == 1);
        boolean hasDate = schedules.stream().anyMatch(s -> s.getType() == 4);
        
        assertTrue(hasBirthday, "应该识别到生日日程");
        assertTrue(hasDate, "应该识别到约会日程");
    }

    @Test
    @DisplayName("测试空文本处理")
    void testEmptyText() {
        String text = "";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        assertNotNull(schedules, "应该返回空列表");
        assertTrue(schedules.isEmpty(), "空文本不应该识别到日程");
    }

    @Test
    @DisplayName("测试null文本处理")
    void testNullText() {
        String text = null;
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        assertNotNull(schedules, "应该返回空列表");
        assertTrue(schedules.isEmpty(), "null文本不应该识别到日程");
    }

    @Test
    @DisplayName("测试无关键词文本")
    void testNoKeywordText() {
        String text = "今天天气很好，心情不错";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        assertNotNull(schedules, "应该返回空列表");
        assertTrue(schedules.isEmpty(), "无关键词文本不应该识别到日程");
    }

    @Test
    @DisplayName("测试日期提取 - 标准格式")
    void testDateExtraction() {
        String text = "2024年12月25日是我女朋友的生日";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        assertNotNull(schedules, "应该返回日程列表");
        assertFalse(schedules.isEmpty(), "应该识别到生日日程");
        
        Schedule schedule = schedules.get(0);
        assertNotNull(schedule.getStartTime(), "应该提取到开始时间");
        assertNotNull(schedule.getEndTime(), "应该提取到结束时间");
    }

    @Test
    @DisplayName("测试时间提取 - 带时间")
    void testTimeExtraction() {
        String text = "明天晚上7点一起看电影";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        assertNotNull(schedules, "应该返回日程列表");
        assertFalse(schedules.isEmpty(), "应该识别到约会日程");
        
        Schedule schedule = schedules.get(0);
        assertNotNull(schedule.getStartTime(), "应该提取到开始时间");
        assertNotNull(schedule.getEndTime(), "应该提取到结束时间");
    }

    @Test
    @DisplayName("测试优先级设置 - 生日")
    void testPrioritySettingBirthday() {
        String text = "明天是我女朋友的生日";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        Schedule schedule = schedules.get(0);
        assertEquals(3, schedule.getPriority(), "生日应该是高优先级");
    }

    @Test
    @DisplayName("测试优先级设置 - 约会")
    void testPrioritySettingDate() {
        String text = "明天晚上一起吃饭";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        Schedule schedule = schedules.get(0);
        assertEquals(2, schedule.getPriority(), "约会应该是中优先级");
    }

    @Test
    @DisplayName("测试优先级设置 - 会议")
    void testPrioritySettingMeeting() {
        String text = "明天上午开会";
        List<Schedule> schedules = importantDateRecognitionService.identifyImportantDates(text);
        
        Schedule schedule = schedules.get(0);
        assertEquals(3, schedule.getPriority(), "会议应该是高优先级");
    }
}
