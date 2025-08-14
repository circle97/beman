package com.beman.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.beman.model.User;
import com.beman.model.dto.UserLoginDTO;
import com.beman.model.dto.UserRegisterDTO;
import com.beman.model.vo.Result;
import com.beman.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        try {
            User user = userService.register(registerDTO);
            // 不返回密码等敏感信息
            user.setPassword(null);
            return Result.success("注册成功", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        try {
            String token = userService.login(loginDTO);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("tokenName", "beman-token");
            return Result.success("登录成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 匿名登录
     */
    @PostMapping("/anonymous-login")
    public Result<Map<String, Object>> anonymousLogin(@RequestParam String deviceId) {
        try {
            String token = userService.anonymousLogin(deviceId);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("tokenName", "beman-token");
            return Result.success("匿名登录成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo() {
        try {
            if (!StpUtil.isLogin()) {
                return Result.error(401, "未登录");
            }
            Long userId = StpUtil.getLoginIdAsLong();
            User user = userService.findById(userId);
            if (user != null) {
                user.setPassword(null);
                return Result.success(user);
            }
            return Result.error("用户不存在");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        try {
            StpUtil.logout();
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查登录状态
     */
    @GetMapping("/check-login")
    public Result<Boolean> checkLogin() {
        return Result.success(StpUtil.isLogin());
    }
} 