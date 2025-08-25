package com.beman.model.dto;

import lombok.Data;

/**
 * 举报查询DTO
 */
@Data
public class ReportQueryDTO {

    /**
     * 举报状态筛选
     */
    private Integer status;

    /**
     * 内容类型筛选
     */
    private Integer contentType;

    /**
     * 优先级筛选
     */
    private Integer priority;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 页码
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer size = 20;
}
