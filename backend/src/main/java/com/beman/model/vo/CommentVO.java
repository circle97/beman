package com.beman.model.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论VO
 */
@Data
public class CommentVO {

    /**
     * 评论ID
     */
    private Long id;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 作者名称
     */
    private String authorName;

    /**
     * 作者头像
     */
    private String authorAvatar;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 回复用户ID
     */
    private Long replyUserId;

    /**
     * 回复用户名称
     */
    private String replyUserName;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 是否匿名
     */
    private Boolean isAnonymous;

    /**
     * 是否已点赞（当前用户）
     */
    private Boolean isLiked;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 子评论列表
     */
    private List<CommentVO> children;

    /**
     * 子评论数量
     */
    private Integer childrenCount;
}
