package com.beman.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 日程查询DTO
 */
@Data
public class ScheduleQueryDTO {

    /**
     * 页码
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer size = 20;

    /**
     * 关键词搜索（标题、描述）
     */
    private String keyword;

    /**
     * 日程类型
     */
    private Integer type;

    /**
     * 重要程度
     */
    private Integer priority;

    /**
     * 开始时间范围：开始
     */
    private LocalDateTime startTimeFrom;

    /**
     * 开始时间范围：结束
     */
    private LocalDateTime startTimeTo;

    /**
     * 结束时间范围：开始
     */
    private LocalDateTime endTimeFrom;

    /**
     * 结束时间范围：结束
     */
    private LocalDateTime endTimeTo;

    /**
     * 日期范围：开始日期
     */
    private LocalDate dateFrom;

    /**
     * 日期范围：结束日期
     */
    private LocalDate dateTo;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 位置
     */
    private String location;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 是否包含重复日程
     */
    private Boolean includeRepeating = true;

    /**
     * 排序字段
     */
    private String sortBy = "startTime";

    /**
     * 排序方向：asc-升序，desc-降序
     */
    private String sortOrder = "asc";
}
