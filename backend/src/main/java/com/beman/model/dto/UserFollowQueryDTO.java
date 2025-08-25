package com.beman.model.dto;

import lombok.Data;

/**
 * 用户关注查询DTO
 */
@Data
public class UserFollowQueryDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 查询类型：1-关注列表，2-粉丝列表
     */
    private Integer queryType = 1;

    /**
     * 页码
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer size = 20;

    /**
     * 关注类型筛选
     */
    private Integer followType;

    /**
     * 关键词搜索（用户名、昵称、备注）
     */
    private String keyword;
}
