package com.beman.model.dto;

import lombok.Data;

/**
 * 评论查询DTO
 */
@Data
public class CommentQueryDTO {

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 父评论ID（用于查询回复）
     */
    private Long parentId;

    /**
     * 页码
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer size = 20;

    /**
     * 排序方式：1-时间正序，2-时间倒序，3-点赞数
     */
    private Integer sortType = 1;
}
