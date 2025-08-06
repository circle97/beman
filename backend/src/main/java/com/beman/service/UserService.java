package com.beman.service;

import com.beman.model.User;
import com.beman.model.dto.UserLoginDTO;
import com.beman.model.dto.UserRegisterDTO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     */
    User register(UserRegisterDTO registerDTO);

    /**
     * 用户登录
     */
    String login(UserLoginDTO loginDTO);

    /**
     * 匿名登录
     */
    String anonymousLogin(String deviceId);

    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);

    /**
     * 根据匿名ID查询用户
     */
    User findByAnonymousId(String anonymousId);

    /**
     * 更新用户信息
     */
    boolean updateUser(User user);

    /**
     * 生成匿名ID
     */
    String generateAnonymousId();

    /**
     * 根据ID查询用户
     */
    User findById(Long id);
} 