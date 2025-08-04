package com.beman.community.dto;

import lombok.Data;
import com.beman.community.entity.CommunityPost.PostType;
import com.beman.community.entity.CommunityPost.PostStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private PostType postType;
    private List<String> tags;
    private boolean isAnonymous;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private PostStatus status;
    private String authorName; // 匿名时显示"匿名用户"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 