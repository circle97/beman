package com.beman.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 用户关注DTO
 */
@Data
public class UserFollowDTO {

    /**
     * 被关注者ID
     */
    @NotNull(message = "被关注者ID不能为空")
    private Long followingId;

    /**
     * 关注类型：1-普通关注，2-特别关注，3-好友关注
     */
    private Integer followType = 1;

    /**
     * 备注名称
     */
    private String remarkName;
}
