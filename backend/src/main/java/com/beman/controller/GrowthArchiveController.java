package com.beman.controller;

import com.beman.model.vo.Result;
import com.beman.model.RelationshipMilestone;
import com.beman.model.GrowthTrajectory;
import com.beman.model.dto.MilestoneCreateDTO;
import com.beman.model.dto.TrajectoryCreateDTO;
import com.beman.service.GrowthArchiveService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Map;

/**
 * 成长档案控制器
 * 
 * @author beman
 * @since 2024-12-25
 */
@Slf4j
@RestController
@RequestMapping("/api/growth-archive")
@CrossOrigin(origins = "*")
public class GrowthArchiveController {

    @Resource
    private GrowthArchiveService growthArchiveService;

    // ==================== 里程碑管理 ====================

    /**
     * 创建里程碑
     */
    @PostMapping("/milestones")
    public Result<?> createMilestone(@RequestParam Long userId, 
                                   @Valid @RequestBody MilestoneCreateDTO dto) {
        try {
            log.info("创建里程碑请求，用户ID: {}, 标题: {}", userId, dto.getTitle());
            RelationshipMilestone milestone = growthArchiveService.createMilestone(userId, dto);
            return Result.success(milestone);
        } catch (Exception e) {
            log.error("创建里程碑失败", e);
            return Result.error("创建里程碑失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户里程碑列表
     */
    @GetMapping("/milestones")
    public Result<?> getUserMilestones(@RequestParam Long userId,
                                     @RequestParam(required = false) String milestoneType) {
        try {
            log.info("获取用户里程碑请求，用户ID: {}, 类型: {}", userId, milestoneType);
            List<RelationshipMilestone> milestones = growthArchiveService.getUserMilestones(userId, milestoneType);
            return Result.success(milestones);
        } catch (Exception e) {
            log.error("获取用户里程碑失败", e);
            return Result.error("获取用户里程碑失败: " + e.getMessage());
        }
    }

    /**
     * 获取里程碑详情
     */
    @GetMapping("/milestones/{milestoneId}")
    public Result<?> getMilestoneDetail(@RequestParam Long userId,
                                      @PathVariable Long milestoneId) {
        try {
            log.info("获取里程碑详情请求，用户ID: {}, 里程碑ID: {}", userId, milestoneId);
            RelationshipMilestone milestone = growthArchiveService.getMilestoneDetail(userId, milestoneId);
            return Result.success(milestone);
        } catch (Exception e) {
            log.error("获取里程碑详情失败", e);
            return Result.error("获取里程碑详情失败: " + e.getMessage());
        }
    }

    /**
     * 更新里程碑
     */
    @PutMapping("/milestones/{milestoneId}")
    public Result<?> updateMilestone(@RequestParam Long userId,
                                   @PathVariable Long milestoneId,
                                   @Valid @RequestBody MilestoneCreateDTO dto) {
        try {
            log.info("更新里程碑请求，用户ID: {}, 里程碑ID: {}", userId, milestoneId);
            RelationshipMilestone milestone = growthArchiveService.updateMilestone(userId, milestoneId, dto);
            return Result.success(milestone);
        } catch (Exception e) {
            log.error("更新里程碑失败", e);
            return Result.error("更新里程碑失败: " + e.getMessage());
        }
    }

    /**
     * 删除里程碑
     */
    @DeleteMapping("/milestones/{milestoneId}")
    public Result<?> deleteMilestone(@RequestParam Long userId,
                                   @PathVariable Long milestoneId) {
        try {
            log.info("删除里程碑请求，用户ID: {}, 里程碑ID: {}", userId, milestoneId);
            boolean success = growthArchiveService.deleteMilestone(userId, milestoneId);
            return success ? Result.success("删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            log.error("删除里程碑失败", e);
            return Result.error("删除里程碑失败: " + e.getMessage());
        }
    }

    // ==================== 成长轨迹管理 ====================

    /**
     * 创建成长轨迹记录
     */
    @PostMapping("/trajectories")
    public Result<?> createTrajectory(@RequestParam Long userId,
                                    @Valid @RequestBody TrajectoryCreateDTO dto) {
        try {
            log.info("创建成长轨迹请求，用户ID: {}, 日期: {}", userId, dto.getRecordDate());
            GrowthTrajectory trajectory = growthArchiveService.createTrajectory(userId, dto);
            return Result.success(trajectory);
        } catch (Exception e) {
            log.error("创建成长轨迹失败", e);
            return Result.error("创建成长轨迹失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户成长轨迹
     */
    @GetMapping("/trajectories")
    public Result<?> getUserTrajectories(@RequestParam Long userId,
                                       @RequestParam(required = false) String startDate,
                                       @RequestParam(required = false) String endDate) {
        try {
            log.info("获取用户成长轨迹请求，用户ID: {}, 日期范围: {} - {}", userId, startDate, endDate);
            
            LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
            
            List<GrowthTrajectory> trajectories = growthArchiveService.getUserTrajectories(userId, start, end);
            return Result.success(trajectories);
        } catch (Exception e) {
            log.error("获取用户成长轨迹失败", e);
            return Result.error("获取用户成长轨迹失败: " + e.getMessage());
        }
    }

    /**
     * 获取最新成长轨迹
     */
    @GetMapping("/trajectories/latest")
    public Result<?> getLatestTrajectories(@RequestParam Long userId,
                                         @RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("获取最新成长轨迹请求，用户ID: {}, 限制数量: {}", userId, limit);
            List<GrowthTrajectory> trajectories = growthArchiveService.getLatestTrajectories(userId, limit);
            return Result.success(trajectories);
        } catch (Exception e) {
            log.error("获取最新成长轨迹失败", e);
            return Result.error("获取最新成长轨迹失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析 ====================

    /**
     * 获取成长档案统计
     */
    @GetMapping("/stats")
    public Result<?> getGrowthStats(@RequestParam Long userId) {
        try {
            log.info("获取成长档案统计请求，用户ID: {}", userId);
            Map<String, Object> stats = growthArchiveService.getGrowthStats(userId);
            return Result.success(stats);
        } catch (Exception e) {
            log.error("获取成长档案统计失败", e);
            return Result.error("获取成长档案统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取成长趋势分析
     */
    @GetMapping("/trend")
    public Result<?> getGrowthTrend(@RequestParam Long userId,
                                   @RequestParam(defaultValue = "30") Integer days) {
        try {
            log.info("获取成长趋势分析请求，用户ID: {}, 天数: {}", userId, days);
            Map<String, Object> trend = growthArchiveService.getGrowthTrend(userId, days);
            return Result.success(trend);
        } catch (Exception e) {
            log.error("获取成长趋势分析失败", e);
            return Result.error("获取成长趋势分析失败: " + e.getMessage());
        }
    }

    /**
     * 获取里程碑统计
     */
    @GetMapping("/stats/milestones")
    public Result<?> getMilestoneStats(@RequestParam Long userId) {
        try {
            log.info("获取里程碑统计请求，用户ID: {}", userId);
            Map<String, Object> stats = growthArchiveService.getMilestoneStats(userId);
            return Result.success(stats);
        } catch (Exception e) {
            log.error("获取里程碑统计失败", e);
            return Result.error("获取里程碑统计失败: " + e.getMessage());
        }
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<?> health() {
        return Result.success("成长档案服务运行正常");
    }
}
