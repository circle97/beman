package com.beman.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beman.mapper.UserMapper;
import com.beman.model.User;
import com.beman.model.dto.UserLoginDTO;
import com.beman.model.dto.UserRegisterDTO;
import com.beman.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public User register(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", registerDTO.getUsername());
        if (userMapper.selectOne(queryWrapper) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查密码确认
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new RuntimeException("两次密码输入不一致");
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(DigestUtils.md5DigestAsHex(registerDTO.getPassword().getBytes()));
        user.setNickname(registerDTO.getNickname());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setUserType(registerDTO.getUserType());
        user.setStatus(1);
        user.setPrivacyLevel(0);
        user.setAnonymousId(generateAnonymousId());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);
        return user;
    }

    @Override
    public String login(UserLoginDTO loginDTO) {
        User user = findByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        String encryptedPassword = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes());
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new RuntimeException("密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已被禁用");
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        // 生成token
        StpUtil.login(user.getId());
        return StpUtil.getTokenValue();
    }

    @Override
    public String anonymousLogin(String deviceId) {
        // 查找或创建匿名用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId).eq("user_type", 2);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            // 创建匿名用户
            user = new User();
            user.setUsername("anonymous_" + deviceId);
            user.setNickname("匿名用户" + deviceId.substring(0, 6));
            user.setUserType(2);
            user.setStatus(1);
            user.setPrivacyLevel(2);
            user.setAnonymousId(generateAnonymousId());
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            userMapper.insert(user);
        }

        // 生成token
        StpUtil.login(user.getId());
        return StpUtil.getTokenValue();
    }

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User findByAnonymousId(String anonymousId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("anonymous_id", anonymousId);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateById(user) > 0;
    }

    @Override
    public String generateAnonymousId() {
        return "anon_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectById(id);
    }
} 