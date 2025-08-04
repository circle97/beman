package com.beman.user;

import com.beman.common.ApiResponse;
import com.beman.user.dto.LoginRequest;
import com.beman.user.dto.LoginResponse;
import com.beman.user.dto.RegisterRequest;
import com.beman.user.dto.UserResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = "用户认证")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success(authService.register(request));
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }

    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public ApiResponse<Void> logout() {
        authService.logout();
        return ApiResponse.success(null);
    }
}