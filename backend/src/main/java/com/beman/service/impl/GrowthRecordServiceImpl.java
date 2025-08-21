package com.beman.service.impl;

import com.beman.mapper.GrowthRecordMapper;
import com.beman.model.GrowthRecord;
import com.beman.service.GrowthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 成长记录服务实现类
 */
@Service
public class GrowthRecordServiceImpl implements GrowthRecordService {
    
    @Autowired
    private GrowthRecordMapper growthRecordMapper;
    
    @Override
    public GrowthRecord createGrowthRecord(GrowthRecord record) {
        // 设置创建时间和更新时间
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        
        // 插入数据库
        growthRecordMapper.insert(record);
        
        return record;
    }
    
    @Override
    public GrowthRecord getGrowthRecordById(Long id) {
        return growthRecordMapper.selectById(id);
    }
    
    @Override
    public List<GrowthRecord> getUserGrowthRecords(Long userId) {
        return growthRecordMapper.selectByUserId(userId);
    }
    
    @Override
    public List<GrowthRecord> getUserGrowthRecordsByType(Long userId, Integer growthType) {
        return growthRecordMapper.selectByUserIdAndType(userId, growthType);
    }
    
    @Override
    public List<GrowthRecord> getUserGrowthRecordsByTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return growthRecordMapper.selectByUserIdAndTimeRange(userId, startTime, endTime);
    }
    
    @Override
    public GrowthRecord updateGrowthRecord(GrowthRecord record) {
        // 设置更新时间
        record.setUpdateTime(LocalDateTime.now());
        
        // 更新数据库
        growthRecordMapper.update(record);
        
        return record;
    }
    
    @Override
    public boolean deleteGrowthRecord(Long id) {
        return growthRecordMapper.deleteById(id) > 0;
    }
    
    @Override
    public Map<String, Object> getUserGrowthStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 总记录数
        int totalRecords = growthRecordMapper.countByUserId(userId);
        stats.put("totalRecords", totalRecords);
        
        // 按类型统计
        List<GrowthRecord> typeStats = growthRecordMapper.countByUserIdAndType(userId);
        Map<Integer, Integer> typeCounts = new HashMap<>();
        Map<Integer, Double> typeAvgScores = new HashMap<>();
        
        for (GrowthRecord record : typeStats) {
            Integer type = record.getGrowthType();
            typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
        }
        
        // 计算各类型平均分数
        List<GrowthRecord> allRecords = getUserGrowthRecords(userId);
        Map<Integer, List<GrowthRecord>> recordsByType = allRecords.stream()
            .collect(Collectors.groupingBy(GrowthRecord::getGrowthType));
        
        for (Map.Entry<Integer, List<GrowthRecord>> entry : recordsByType.entrySet()) {
            Integer type = entry.getKey();
            List<GrowthRecord> records = entry.getValue();
            double avgScore = records.stream()
                .mapToInt(GrowthRecord::getScore)
                .average()
                .orElse(0.0);
            typeAvgScores.put(type, Math.round(avgScore * 100.0) / 100.0);
        }
        
        stats.put("typeCounts", typeCounts);
        stats.put("typeAvgScores", typeAvgScores);
        
        // 总体平均分数
        double overallAvgScore = allRecords.stream()
            .mapToInt(GrowthRecord::getScore)
            .average()
            .orElse(0.0);
        stats.put("overallAvgScore", Math.round(overallAvgScore * 100.0) / 100.0);
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getUserScoreTrend(Long userId, Integer days) {
        Map<String, Object> trend = new HashMap<>();
        
        // 获取指定天数的成长记录
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(days);
        
        List<GrowthRecord> records = getUserGrowthRecordsByTimeRange(userId, startTime, endTime);
        
        // 按日期分组并计算平均分数
        Map<String, List<GrowthRecord>> recordsByDate = records.stream()
            .collect(Collectors.groupingBy(record -> 
                record.getRecordTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        
        Map<String, Double> dailyScores = new LinkedHashMap<>();
        for (int i = days - 1; i >= 0; i--) {
            LocalDateTime date = endTime.minusDays(i);
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            List<GrowthRecord> dayRecords = recordsByDate.get(dateStr);
            if (dayRecords != null && !dayRecords.isEmpty()) {
                double avgScore = dayRecords.stream()
                    .mapToInt(GrowthRecord::getScore)
                    .average()
                    .orElse(0.0);
                dailyScores.put(dateStr, Math.round(avgScore * 100.0) / 100.0);
            } else {
                dailyScores.put(dateStr, 0.0);
            }
        }
        
        trend.put("dailyScores", dailyScores);
        trend.put("totalDays", days);
        
        return trend;
    }
    
    @Override
    public Map<String, Object> getUserGrowthCurve(Long userId, Integer months) {
        Map<String, Object> curve = new HashMap<>();
        
        // 获取指定月数的成长记录
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusMonths(months);
        
        List<GrowthRecord> records = getUserGrowthRecordsByTimeRange(userId, startTime, endTime);
        
        // 按月份分组并计算平均分数
        Map<String, List<GrowthRecord>> recordsByMonth = records.stream()
            .collect(Collectors.groupingBy(record -> 
                record.getRecordTime().format(DateTimeFormatter.ofPattern("yyyy-MM"))));
        
        Map<String, Double> monthlyScores = new LinkedHashMap<>();
        for (int i = months - 1; i >= 0; i--) {
            LocalDateTime month = endTime.minusMonths(i);
            String monthStr = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            
            List<GrowthRecord> monthRecords = recordsByMonth.get(monthStr);
            if (monthRecords != null && !monthRecords.isEmpty()) {
                double avgScore = monthRecords.stream()
                    .mapToInt(GrowthRecord::getScore)
                    .average()
                    .orElse(0.0);
                monthlyScores.put(monthStr, Math.round(avgScore * 100.0) / 100.0);
            } else {
                monthlyScores.put(monthStr, 0.0);
            }
        }
        
        curve.put("monthlyScores", monthlyScores);
        curve.put("totalMonths", months);
        
        // 计算成长趋势
        List<Double> scores = new ArrayList<>(monthlyScores.values());
        if (scores.size() >= 2) {
            double trend = calculateTrend(scores);
            curve.put("trend", Math.round(trend * 100.0) / 100.0);
            curve.put("trendDescription", getTrendDescription(trend));
        }
        
        return curve;
    }
    
    @Override
    public List<GrowthRecord> autoGenerateGrowthRecords(Long userId) {
        List<GrowthRecord> newRecords = new ArrayList<>();
        
        // 这里应该根据用户的实际行为数据来生成成长记录
        // 例如：发帖数量、使用工具次数、完成计划数量等
        // 暂时返回空列表，后续可以扩展
        
        return newRecords;
    }
    
    @Override
    public Map<Integer, String> getGrowthTypeMapping() {
        Map<Integer, String> mapping = new HashMap<>();
        mapping.put(1, "沟通改善");
        mapping.put(2, "情绪管理");
        mapping.put(3, "关系维护");
        mapping.put(4, "个人成长");
        mapping.put(5, "财务透明");
        mapping.put(6, "日程管理");
        return mapping;
    }
    
    /**
     * 计算成长趋势
     * @param scores 分数列表
     * @return 趋势值（正数表示上升，负数表示下降）
     */
    private double calculateTrend(List<Double> scores) {
        if (scores.size() < 2) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (int i = 1; i < scores.size(); i++) {
            sum += scores.get(i) - scores.get(i - 1);
        }
        
        return sum / (scores.size() - 1);
    }
    
    /**
     * 获取趋势描述
     * @param trend 趋势值
     * @return 趋势描述
     */
    private String getTrendDescription(double trend) {
        if (trend > 5) {
            return "快速上升";
        } else if (trend > 1) {
            return "稳步上升";
        } else if (trend > -1) {
            return "保持稳定";
        } else if (trend > -5) {
            return "略有下降";
        } else {
            return "需要关注";
        }
    }
}
