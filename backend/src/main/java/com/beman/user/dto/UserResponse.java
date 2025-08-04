package com.beman.user.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private String relationshipStatus;
    private LocalDateTime createdAt;
} 