package com.beman.service.impl;

import com.beman.model.PracticePlan;
import com.beman.service.PracticePlanService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 实践计划服务实现类
 */
@Service
public class PracticePlanServiceImpl implements PracticePlanService {
    
    // 使用内存存储，实际项目中应该使用数据库
    private final Map<Long, PracticePlan> planStore = new ConcurrentHashMap<>();
    private final Map<Long, List<Long>> userPlanMap = new ConcurrentHashMap<>();
    private long nextId = 1;
    
    @Override
    public PracticePlan createPlan(PracticePlan plan) {
        plan.setId(nextId++);
        plan.setCreateTime(LocalDateTime.now());
        plan.setUpdateTime(LocalDateTime.now());
        
        // 存储计划
        planStore.put(plan.getId(), plan);
        
        // 更新用户计划映射
        userPlanMap.computeIfAbsent(plan.getUserId(), k -> new ArrayList<>())
                  .add(plan.getId());
        
        return plan;
    }
    
    @Override
    public PracticePlan updatePlan(PracticePlan plan) {
        PracticePlan existingPlan = planStore.get(plan.getId());
        if (existingPlan == null) {
            throw new RuntimeException("实践计划不存在");
        }
        
        // 更新字段
        existingPlan.setTitle(plan.getTitle());
        existingPlan.setDescription(plan.getDescription());
        existingPlan.setType(plan.getType());
        existingPlan.setStatus(plan.getStatus());
        existingPlan.setPriority(plan.getPriority());
        existingPlan.setExpectedEndTime(plan.getExpectedEndTime());
        existingPlan.setRemark(plan.getRemark());
        existingPlan.setTags(plan.getTags());
        existingPlan.setProgress(plan.getProgress());
        existingPlan.setIsPublic(plan.getIsPublic());
        existingPlan.setUpdateTime(LocalDateTime.now());
        
        // 如果状态变为已完成，设置实际完成时间
        if (plan.getStatus() != null && plan.getStatus() == 2) {
            existingPlan.setActualEndTime(LocalDateTime.now());
        }
        
        return existingPlan;
    }
    
    @Override
    public List<PracticePlan> getUserPlans(Long userId) {
        List<Long> planIds = userPlanMap.get(userId);
        if (planIds == null) {
            return new ArrayList<>();
        }
        
        return planIds.stream()
                .map(planStore::get)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(PracticePlan::getCreateTime).reversed())
                .collect(Collectors.toList());
    }
    
    @Override
    public PracticePlan getPlanById(Long planId) {
        return planStore.get(planId);
    }
    
    @Override
    public boolean deletePlan(Long planId) {
        PracticePlan plan = planStore.get(planId);
        if (plan == null) {
            return false;
        }
        
        // 从用户计划映射中移除
        List<Long> userPlans = userPlanMap.get(plan.getUserId());
        if (userPlans != null) {
            userPlans.remove(planId);
        }
        
        // 从存储中移除
        planStore.remove(planId);
        
        return true;
    }
    
    @Override
    public boolean updateProgress(Long planId, Integer progress) {
        PracticePlan plan = planStore.get(planId);
        if (plan == null) {
            return false;
        }
        
        plan.setProgress(Math.max(0, Math.min(100, progress)));
        plan.setUpdateTime(LocalDateTime.now());
        
        // 如果进度达到100%，自动设置为已完成
        if (plan.getProgress() >= 100 && plan.getStatus() != 2) {
            plan.setStatus(2);
            plan.setActualEndTime(LocalDateTime.now());
        }
        
        return true;
    }
    
    @Override
    public boolean updateStatus(Long planId, Integer status) {
        PracticePlan plan = planStore.get(planId);
        if (plan == null) {
            return false;
        }
        
        plan.setStatus(status);
        plan.setUpdateTime(LocalDateTime.now());
        
        // 如果状态变为已完成，设置实际完成时间
        if (status == 2) {
            plan.setActualEndTime(LocalDateTime.now());
        }
        
        return true;
    }
    
    @Override
    public List<PracticePlan> getPublicPlans(Long userId) {
        return getUserPlans(userId).stream()
                .filter(plan -> plan.getIsPublic() == 1)
                .collect(Collectors.toList());
    }
    
    @Override
    public PlanStatistics getPlanStatistics(Long userId) {
        List<PracticePlan> userPlans = getUserPlans(userId);
        
        int totalPlans = userPlans.size();
        int completedPlans = (int) userPlans.stream()
                .filter(plan -> plan.getStatus() == 2)
                .count();
        int inProgressPlans = (int) userPlans.stream()
                .filter(plan -> plan.getStatus() == 1)
                .count();
        int abandonedPlans = (int) userPlans.stream()
                .filter(plan -> plan.getStatus() == 3)
                .count();
        
        return new PlanStatistics(totalPlans, completedPlans, inProgressPlans, abandonedPlans);
    }
}
