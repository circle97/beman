package com.beman.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录DTO
 */
@Data
public class UserLoginDTO {

    /**
     * 用户名/邮箱/手机号
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 登录类型：1-用户名密码，2-匿名登录
     */
    private Integer loginType = 1;

    /**
     * 设备标识（用于匿名登录）
     */
    private String deviceId;
} 