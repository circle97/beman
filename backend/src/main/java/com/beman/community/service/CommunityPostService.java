package com.beman.community.service;

import com.beman.common.ApiResponse;
import com.beman.common.BusinessException;
import com.beman.community.dto.PostRequest;
import com.beman.community.dto.PostResponse;
import com.beman.community.entity.CommunityPost;
import com.beman.community.entity.CommunityPost.PostStatus;
import com.beman.community.entity.CommunityPost.PostType;
import com.beman.community.repository.CommunityPostRepository;
import com.beman.user.entity.User;
import com.beman.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityPostService {
    
    private final CommunityPostRepository postRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final ContentFilterService contentFilterService;
    
    @Transactional
    public PostResponse createPost(PostRequest request, Long userId) {
        // 内容安全检查
        if (!contentFilterService.isContentSafe(request.getTitle() + " " + request.getContent())) {
            throw new BusinessException("内容包含不当词汇，请修改后重新发布");
        }
        
        CommunityPost post = new CommunityPost();
        post.setUserId(userId);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setPostType(request.getPostType());
        post.setAnonymous(request.isAnonymous());
        post.setStatus(PostStatus.APPROVED); // 简化处理，直接通过
        
        // 处理标签
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            try {
                post.setTags(objectMapper.writeValueAsString(request.getTags()));
            } catch (JsonProcessingException e) {
                throw new BusinessException("标签格式错误");
            }
        }
        
        CommunityPost savedPost = postRepository.save(post);
        return convertToResponse(savedPost);
    }
    
    public Page<PostResponse> getPosts(PostType postType, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommunityPost> posts;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            posts = postRepository.searchByKeyword(keyword.trim(), PostStatus.APPROVED, pageable);
        } else if (postType != null) {
            posts = postRepository.findByPostTypeAndStatusOrderByCreatedAtDesc(postType, PostStatus.APPROVED, pageable);
        } else {
            posts = postRepository.findByStatusOrderByCreatedAtDesc(PostStatus.APPROVED, pageable);
        }
        
        return posts.map(this::convertToResponse);
    }
    
    public PostResponse getPostById(Long postId) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException("帖子不存在"));
        
        if (post.getStatus() != PostStatus.APPROVED) {
            throw new BusinessException("帖子不存在或已被删除");
        }
        
        // 增加浏览次数
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        
        return convertToResponse(post);
    }
    
    public List<PostResponse> getUserPosts(Long userId) {
        List<CommunityPost> posts = postRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, PostStatus.APPROVED);
        return posts.stream().map(this::convertToResponse).toList();
    }
    
    @Transactional
    public void deletePost(Long postId, Long userId) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException("帖子不存在"));
        
        if (!post.getUserId().equals(userId)) {
            throw new BusinessException("无权限删除此帖子");
        }
        
        postRepository.delete(post);
    }
    
    private PostResponse convertToResponse(CommunityPost post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setPostType(post.getPostType());
        response.setAnonymous(post.isAnonymous());
        response.setViewCount(post.getViewCount());
        response.setLikeCount(post.getLikeCount());
        response.setCommentCount(post.getCommentCount());
        response.setStatus(post.getStatus());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        
        // 处理作者名称
        if (post.isAnonymous()) {
            response.setAuthorName("匿名用户");
        } else {
            Optional<User> user = userRepository.findById(post.getUserId());
            response.setAuthorName(user.map(User::getNickname).orElse("未知用户"));
        }
        
        // 处理标签
        if (post.getTags() != null && !post.getTags().isEmpty()) {
            try {
                List<String> tags = objectMapper.readValue(post.getTags(), new TypeReference<List<String>>() {});
                response.setTags(tags);
            } catch (JsonProcessingException e) {
                response.setTags(List.of());
            }
        }
        
        return response;
    }
} 