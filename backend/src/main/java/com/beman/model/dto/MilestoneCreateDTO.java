package com.beman.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * 里程碑创建DTO
 * 
 * @author beman
 * @since 2024-12-25
 */
@Data
public class MilestoneCreateDTO {

    /**
     * 里程碑类型
     */
    @NotBlank(message = "里程碑类型不能为空")
    private String milestoneType;

    /**
     * 里程碑标题
     */
    @NotBlank(message = "里程碑标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200字符")
    private String title;

    /**
     * 里程碑描述
     */
    @Size(max = 1000, message = "描述长度不能超过1000字符")
    private String description;

    /**
     * 里程碑日期
     */
    @NotNull(message = "里程碑日期不能为空")
    private LocalDate milestoneDate;

    /**
     * 地点
     */
    @Size(max = 200, message = "地点长度不能超过200字符")
    private String location;

    /**
     * 情感评分(1-10)
     */
    @Size(min = 1, max = 10, message = "情感评分必须在1-10之间")
    private Integer emotionScore;

    /**
     * 照片URL数组
     */
    private List<String> photos;

    /**
     * 标签数组
     */
    private List<String> tags;

    /**
     * 是否公开：0-私密，1-公开
     */
    private Integer isPublic = 1;
}
