package com.beman.community.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "community_posts")
@EntityListeners(AuditingEntityListener.class)
public class CommunityPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "post_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Column(columnDefinition = "JSON")
    private String tags;

    @Column(name = "is_anonymous")
    private boolean isAnonymous = true;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Column(name = "comment_count")
    private Integer commentCount = 0;

    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.PENDING;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum PostType {
        HELP, EXPERIENCE, SHARE
    }

    public enum PostStatus {
        PENDING, APPROVED, REJECTED
    }
} 