package com.beman.service;

import com.beman.model.Schedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 重要日期识别服务
 */
@Slf4j
@Service
public class ImportantDateRecognitionService {

    // 生日关键词
    private static final String[] BIRTHDAY_KEYWORDS = {
        "生日", "出生", "诞辰", "寿辰", "满月", "周岁", "birthday", "birth"
    };

    // 纪念日关键词
    private static final String[] ANNIVERSARY_KEYWORDS = {
        "纪念日", "周年", "结婚", "恋爱", "相识", "毕业", "入职", "开业", "anniversary"
    };

    // 节日关键词
    private static final String[] FESTIVAL_KEYWORDS = {
        "春节", "中秋", "国庆", "元旦", "圣诞", "情人节", "母亲节", "父亲节", "儿童节"
    };

    // 约会关键词
    private static final String[] DATE_KEYWORDS = {
        "约会", "见面", "聚餐", "吃饭", "看电影", "逛街", "date", "meeting"
    };

    // 会议关键词
    private static final String[] MEETING_KEYWORDS = {
        "会议", "开会", "讨论", "汇报", "培训", "meeting", "conference"
    };

    // 日期正则表达式
    private static final Pattern DATE_PATTERN = Pattern.compile(
        "(\\d{4})[年\\-/](\\d{1,2})[月\\-/](\\d{1,2})[日号]?"
    );
    
    private static final Pattern TIME_PATTERN = Pattern.compile(
        "(\\d{1,2})[时:](\\d{1,2})[分]?"
    );

    /**
     * 识别文本中的重要日期
     */
    public List<Schedule> identifyImportantDates(String text) {
        List<Schedule> schedules = new ArrayList<>();
        
        if (text == null || text.trim().isEmpty()) {
            return schedules;
        }

        try {
            // 识别生日
            schedules.addAll(identifyBirthdays(text));
            
            // 识别纪念日
            schedules.addAll(identifyAnniversaries(text));
            
            // 识别节日
            schedules.addAll(identifyFestivals(text));
            
            // 识别约会
            schedules.addAll(identifyDates(text));
            
            // 识别会议
            schedules.addAll(identifyMeetings(text));
            
        } catch (Exception e) {
            log.error("识别重要日期时发生错误", e);
        }

        return schedules;
    }

    /**
     * 识别生日
     */
    private List<Schedule> identifyBirthdays(String text) {
        List<Schedule> schedules = new ArrayList<>();
        
        for (String keyword : BIRTHDAY_KEYWORDS) {
            if (text.toLowerCase().contains(keyword.toLowerCase())) {
                // 尝试提取日期
                LocalDateTime birthdayDate = extractDateFromText(text);
                if (birthdayDate == null) {
                    // 如果没有具体日期，设置为明天
                    birthdayDate = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0);
                }
                
                Schedule schedule = new Schedule();
                schedule.setTitle("生日提醒");
                schedule.setDescription("识别到的生日：" + keyword + " - " + text);
                schedule.setType(1); // 生日类型
                schedule.setPriority(3); // 高优先级
                schedule.setStartTime(birthdayDate);
                schedule.setEndTime(birthdayDate.withHour(23).withMinute(59));
                schedule.setIsAllDay(true);
                schedule.setRepeatType(4); // 每年重复
                schedule.setStatus(1);
                schedules.add(schedule);
            }
        }
        
        return schedules;
    }

    /**
     * 识别纪念日
     */
    private List<Schedule> identifyAnniversaries(String text) {
        List<Schedule> schedules = new ArrayList<>();
        
        for (String keyword : ANNIVERSARY_KEYWORDS) {
            if (text.toLowerCase().contains(keyword.toLowerCase())) {
                LocalDateTime anniversaryDate = extractDateFromText(text);
                if (anniversaryDate == null) {
                    anniversaryDate = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0);
                }
                
                Schedule schedule = new Schedule();
                schedule.setTitle("纪念日提醒");
                schedule.setDescription("识别到的纪念日：" + keyword + " - " + text);
                schedule.setType(2); // 纪念日类型
                schedule.setPriority(3); // 高优先级
                schedule.setStartTime(anniversaryDate);
                schedule.setEndTime(anniversaryDate.withHour(23).withMinute(59));
                schedule.setIsAllDay(true);
                schedule.setRepeatType(4); // 每年重复
                schedule.setStatus(1);
                schedules.add(schedule);
            }
        }
        
        return schedules;
    }

    /**
     * 识别节日
     */
    private List<Schedule> identifyFestivals(String text) {
        List<Schedule> schedules = new ArrayList<>();
        
        for (String keyword : FESTIVAL_KEYWORDS) {
            if (text.toLowerCase().contains(keyword.toLowerCase())) {
                LocalDateTime festivalDate = calculateFestivalDate(keyword);
                
                Schedule schedule = new Schedule();
                schedule.setTitle(keyword + "提醒");
                schedule.setDescription("识别到的节日：" + keyword + " - " + text);
                schedule.setType(3); // 节日类型
                schedule.setPriority(2); // 中优先级
                schedule.setStartTime(festivalDate);
                schedule.setEndTime(festivalDate.withHour(23).withMinute(59));
                schedule.setIsAllDay(true);
                schedule.setRepeatType(4); // 每年重复
                schedule.setStatus(1);
                schedules.add(schedule);
            }
        }
        
        return schedules;
    }

    /**
     * 识别约会
     */
    private List<Schedule> identifyDates(String text) {
        List<Schedule> schedules = new ArrayList<>();
        
        for (String keyword : DATE_KEYWORDS) {
            if (text.toLowerCase().contains(keyword.toLowerCase())) {
                LocalDateTime dateTime = extractDateTimeFromText(text);
                if (dateTime == null) {
                    dateTime = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0);
                }
                
                Schedule schedule = new Schedule();
                schedule.setTitle("约会提醒");
                schedule.setDescription("识别到的约会：" + keyword + " - " + text);
                schedule.setType(4); // 约会类型
                schedule.setPriority(2); // 中优先级
                schedule.setStartTime(dateTime);
                schedule.setEndTime(dateTime.plusHours(2));
                schedule.setIsAllDay(false);
                schedule.setRepeatType(0); // 不重复
                schedule.setStatus(1);
                schedules.add(schedule);
            }
        }
        
        return schedules;
    }

    /**
     * 识别会议
     */
    private List<Schedule> identifyMeetings(String text) {
        List<Schedule> schedules = new ArrayList<>();
        
        for (String keyword : MEETING_KEYWORDS) {
            if (text.toLowerCase().contains(keyword.toLowerCase())) {
                LocalDateTime meetingTime = extractDateTimeFromText(text);
                if (meetingTime == null) {
                    meetingTime = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0);
                }
                
                Schedule schedule = new Schedule();
                schedule.setTitle("会议提醒");
                schedule.setDescription("识别到的会议：" + keyword + " - " + text);
                schedule.setType(5); // 会议类型
                schedule.setPriority(3); // 高优先级
                schedule.setStartTime(meetingTime);
                schedule.setEndTime(meetingTime.plusHours(1));
                schedule.setIsAllDay(false);
                schedule.setRepeatType(0); // 不重复
                schedule.setStatus(1);
                schedules.add(schedule);
            }
        }
        
        return schedules;
    }

    /**
     * 从文本中提取日期
     */
    private LocalDateTime extractDateFromText(String text) {
        Matcher matcher = DATE_PATTERN.matcher(text);
        if (matcher.find()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            
            try {
                LocalDate date = LocalDate.of(year, month, day);
                // 如果日期已经过去，设置为明年
                if (date.isBefore(LocalDate.now())) {
                    date = date.plusYears(1);
                }
                return date.atStartOfDay();
            } catch (Exception e) {
                log.warn("无效的日期格式: {}-{}-{}", year, month, day);
            }
        }
        return null;
    }

    /**
     * 从文本中提取日期时间
     */
    private LocalDateTime extractDateTimeFromText(String text) {
        LocalDateTime dateTime = extractDateFromText(text);
        if (dateTime != null) {
            // 尝试提取时间
            Matcher timeMatcher = TIME_PATTERN.matcher(text);
            if (timeMatcher.find()) {
                int hour = Integer.parseInt(timeMatcher.group(1));
                int minute = Integer.parseInt(timeMatcher.group(2));
                dateTime = dateTime.withHour(hour).withMinute(minute);
            }
        }
        return dateTime;
    }

    /**
     * 计算节日日期（简化实现）
     */
    private LocalDateTime calculateFestivalDate(String festival) {
        LocalDate now = LocalDate.now();
        LocalDate festivalDate = now;
        
        switch (festival) {
            case "春节":
                // 简化：设置为明年2月1日
                festivalDate = LocalDate.of(now.getYear() + 1, 2, 1);
                break;
            case "中秋":
                // 简化：设置为今年9月15日
                festivalDate = LocalDate.of(now.getYear(), 9, 15);
                if (festivalDate.isBefore(now)) {
                    festivalDate = festivalDate.plusYears(1);
                }
                break;
            case "国庆":
                // 设置为今年10月1日
                festivalDate = LocalDate.of(now.getYear(), 10, 1);
                if (festivalDate.isBefore(now)) {
                    festivalDate = festivalDate.plusYears(1);
                }
                break;
            case "元旦":
                // 设置为明年1月1日
                festivalDate = LocalDate.of(now.getYear() + 1, 1, 1);
                break;
            case "圣诞":
                // 设置为今年12月25日
                festivalDate = LocalDate.of(now.getYear(), 12, 25);
                if (festivalDate.isBefore(now)) {
                    festivalDate = festivalDate.plusYears(1);
                }
                break;
            default:
                // 其他节日设置为明天
                festivalDate = now.plusDays(1);
        }
        
        return festivalDate.atStartOfDay();
    }
}
