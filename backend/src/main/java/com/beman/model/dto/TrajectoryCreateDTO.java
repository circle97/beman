package com.beman.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 成长轨迹创建DTO
 * 
 * @author beman
 * @since 2024-12-25
 */
@Data
public class TrajectoryCreateDTO {

    /**
     * 记录日期
     */
    @NotNull(message = "记录日期不能为空")
    private LocalDate recordDate;

    /**
     * 沟通质量评分(1-100)
     */
    @Size(min = 1, max = 100, message = "沟通质量评分必须在1-100之间")
    private Integer communicationScore;

    /**
     * 信任程度评分(1-100)
     */
    @Size(min = 1, max = 100, message = "信任程度评分必须在1-100之间")
    private Integer trustScore;

    /**
     * 相互支持评分(1-100)
     */
    @Size(min = 1, max = 100, message = "相互支持评分必须在1-100之间")
    private Integer supportScore;

    /**
     * 亲密度评分(1-100)
     */
    @Size(min = 1, max = 100, message = "亲密度评分必须在1-100之间")
    private Integer intimacyScore;

    /**
     * 情绪状态
     */
    private String moodState;

    /**
     * 关系笔记
     */
    @Size(max = 2000, message = "关系笔记长度不能超过2000字符")
    private String relationshipNotes;

    /**
     * 改进目标
     */
    @Size(max = 1000, message = "改进目标长度不能超过1000字符")
    private String improvementGoals;
}
