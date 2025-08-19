package com.beman.service;

import com.beman.model.PracticePlan;

import java.util.List;

/**
 * 实践计划服务接口
 * 负责用户实践计划的管理和跟踪
 */
public interface PracticePlanService {
    
    /**
     * 创建实践计划
     * @param plan 实践计划
     * @return 创建后的计划
     */
    PracticePlan createPlan(PracticePlan plan);
    
    /**
     * 更新实践计划
     * @param plan 更新的计划
     * @return 更新后的计划
     */
    PracticePlan updatePlan(PracticePlan plan);
    
    /**
     * 获取用户的实践计划列表
     * @param userId 用户ID
     * @return 计划列表
     */
    List<PracticePlan> getUserPlans(Long userId);
    
    /**
     * 获取实践计划详情
     * @param planId 计划ID
     * @return 计划详情
     */
    PracticePlan getPlanById(Long planId);
    
    /**
     * 删除实践计划
     * @param planId 计划ID
     * @return 是否删除成功
     */
    boolean deletePlan(Long planId);
    
    /**
     * 更新计划进度
     * @param planId 计划ID
     * @param progress 进度百分比
     * @return 是否更新成功
     */
    boolean updateProgress(Long planId, Integer progress);
    
    /**
     * 更新计划状态
     * @param planId 计划ID
     * @param status 新状态
     * @return 是否更新成功
     */
    boolean updateStatus(Long planId, Integer status);
    
    /**
     * 获取用户公开的计划列表（用于社区展示）
     * @param userId 用户ID
     * @return 公开的计划列表
     */
    List<PracticePlan> getPublicPlans(Long userId);
    
    /**
     * 获取计划统计信息
     * @param userId 用户ID
     * @return 统计信息
     */
    PlanStatistics getPlanStatistics(Long userId);
    
    /**
     * 计划统计信息
     */
    class PlanStatistics {
        private int totalPlans;
        private int completedPlans;
        private int inProgressPlans;
        private int abandonedPlans;
        private double completionRate;
        
        // 构造函数、getter和setter
        public PlanStatistics() {}
        
        public PlanStatistics(int totalPlans, int completedPlans, int inProgressPlans, int abandonedPlans) {
            this.totalPlans = totalPlans;
            this.completedPlans = completedPlans;
            this.inProgressPlans = inProgressPlans;
            this.abandonedPlans = abandonedPlans;
            this.completionRate = totalPlans > 0 ? (double) completedPlans / totalPlans * 100 : 0;
        }
        
        // Getters and Setters
        public int getTotalPlans() { return totalPlans; }
        public void setTotalPlans(int totalPlans) { this.totalPlans = totalPlans; }
        
        public int getCompletedPlans() { return completedPlans; }
        public void setCompletedPlans(int completedPlans) { this.completedPlans = completedPlans; }
        
        public int getInProgressPlans() { return inProgressPlans; }
        public void setInProgressPlans(int inProgressPlans) { this.inProgressPlans = inProgressPlans; }
        
        public int getAbandonedPlans() { return abandonedPlans; }
        public void setAbandonedPlans(int abandonedPlans) { this.abandonedPlans = abandonedPlans; }
        
        public double getCompletionRate() { return completionRate; }
        public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
    }
}
