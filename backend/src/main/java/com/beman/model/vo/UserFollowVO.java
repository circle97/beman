package com.beman.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户关注VO
 */
@Data
public class UserFollowVO {

    /**
     * 关注关系ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 关注类型：1-普通关注，2-特别关注，3-好友关注
     */
    private Integer followType;

    /**
     * 备注名称
     */
    private String remarkName;

    /**
     * 关注时间
     */
    private LocalDateTime createTime;

    /**
     * 是否互相关注
     */
    private Boolean isMutualFollow;

    /**
     * 用户状态
     */
    private Integer userStatus;

    /**
     * 最后活跃时间
     */
    private LocalDateTime lastActiveTime;
}
