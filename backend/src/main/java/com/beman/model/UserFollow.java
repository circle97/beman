package com.beman.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户关注关系实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_follow")
public class UserFollow {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关注者ID
     */
    @TableField("follower_id")
    private Long followerId;

    /**
     * 被关注者ID
     */
    @TableField("following_id")
    private Long followingId;

    /**
     * 关注状态：0-已关注，1-已取消，2-已屏蔽
     */
    @TableField("status")
    private Integer status;

    /**
     * 关注类型：1-普通关注，2-特别关注，3-好友关注
     */
    @TableField("follow_type")
    private Integer followType;

    /**
     * 备注名称
     */
    @TableField("remark_name")
    private String remarkName;

    /**
     * 关注时间
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
