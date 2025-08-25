package com.beman.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 评论实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("comment")
public class Comment {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 帖子ID
     */
    @TableField("post_id")
    private Long postId;

    /**
     * 评论内容
     */
    @TableField("content")
    private String content;

    /**
     * 作者ID
     */
    @TableField("author_id")
    private Long authorId;

    /**
     * 作者名称
     */
    @TableField("author_name")
    private String authorName;

    /**
     * 作者头像
     */
    @TableField("author_avatar")
    private String authorAvatar;

    /**
     * 父评论ID（用于回复功能）
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 回复用户ID
     */
    @TableField("reply_user_id")
    private Long replyUserId;

    /**
     * 回复用户名称
     */
    @TableField("reply_user_name")
    private String replyUserName;

    /**
     * 点赞数
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 是否匿名
     */
    @TableField("is_anonymous")
    private Boolean isAnonymous;

    /**
     * 状态：0-正常，1-已删除，2-已隐藏
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标识
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
