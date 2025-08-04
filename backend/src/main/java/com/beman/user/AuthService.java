package com.beman.user;

import com.beman.common.BusinessException;
import com.beman.user.dto.LoginRequest;
import com.beman.user.dto.LoginResponse;
import com.beman.user.dto.RegisterRequest;
import com.beman.user.dto.UserDetailsImpl;
import com.beman.user.dto.UserResponse;
import com.beman.user.entity.User;
import com.beman.user.repository.UserRepository;
import com.beman.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw BusinessException.badRequest("用户名已存在");
        }

        // 检查手机号是否已存在
        if (userRepository.existsByPhone(request.getPhone())) {
            throw BusinessException.badRequest("手机号已被注册");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setNickname(request.getNickname());
        user.setRelationshipStatus(User.RelationshipStatus.valueOf(request.getRelationshipStatus()));

        User savedUser = userRepository.save(user);

        // 转换为响应DTO
        return convertToUserResponse(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        // 认证用户
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 设置认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成JWT令牌
        String jwt = jwtUtils.generateToken(authentication);

        // 获取用户信息
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        // 更新最后登录时间
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        // 返回登录响应
        return LoginResponse.builder()
                .token(jwt)
                .user(convertToUserResponse(user))
                .build();
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .relationshipStatus(user.getRelationshipStatus().name())
                .createdAt(user.getCreatedAt())
                .build();
    }
}