package com.beman.community.repository;

import com.beman.community.entity.CommunityPost;
import com.beman.community.entity.CommunityPost.PostStatus;
import com.beman.community.entity.CommunityPost.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    
    Page<CommunityPost> findByStatusOrderByCreatedAtDesc(PostStatus status, Pageable pageable);
    
    Page<CommunityPost> findByPostTypeAndStatusOrderByCreatedAtDesc(PostType postType, PostStatus status, Pageable pageable);
    
    @Query("SELECT p FROM CommunityPost p WHERE p.status = :status AND " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY p.createdAt DESC")
    Page<CommunityPost> searchByKeyword(@Param("keyword") String keyword, @Param("status") PostStatus status, Pageable pageable);
    
    List<CommunityPost> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, PostStatus status);
    
    @Query("SELECT p FROM CommunityPost p WHERE p.status = :status AND p.tags LIKE %:tag% ORDER BY p.createdAt DESC")
    Page<CommunityPost> findByTag(@Param("tag") String tag, @Param("status") PostStatus status, Pageable pageable);
} 