package com.beman.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 帖子查询DTO
 */
@Data
public class PostQueryDTO {

    /**
     * 页码
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 排序方式：1-最新，2-最热，3-最多评论
     */
    private Integer sortType = 1;

    /**
     * 内容类型：1-文字，2-语音，null-全部
     */
    private Integer contentType;
}
