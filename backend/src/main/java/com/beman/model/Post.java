package com.beman.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 帖子实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("post")
public class Post {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 匿名ID（匿名发帖时使用）
     */
    @TableField("anonymous_id")
    private String anonymousId;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 内容类型：1-文字，2-语音
     */
    @TableField("content_type")
    private Integer contentType;

    /**
     * 语音文件URL
     */
    @TableField("voice_url")
    private String voiceUrl;

    /**
     * 标签（JSON格式）
     */
    @TableField("tags")
    private String tags;

    /**
     * 浏览次数
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 点赞次数
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 评论次数
     */
    @TableField("comment_count")
    private Integer commentCount;

    /**
     * 状态：0-审核中，1-正常，2-已删除
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否置顶：0-否，1-是
     */
    @TableField("is_top")
    private Integer isTop;

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
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    // 关联字段（非数据库字段）
    @TableField(exist = false)
    private String authorName;

    @TableField(exist = false)
    private String authorAvatar;

    @TableField(exist = false)
    private Boolean isLiked;
}
