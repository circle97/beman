package com.beman.community.controller;

import com.beman.common.ApiResponse;
import com.beman.community.dto.PostRequest;
import com.beman.community.dto.PostResponse;
import com.beman.community.entity.CommunityPost.PostType;
import com.beman.community.service.CommunityPostService;
import com.beman.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {
    
    private final CommunityPostService postService;
    
    @PostMapping("/posts")
    public ApiResponse<PostResponse> createPost(@Valid @RequestBody PostRequest request, @CurrentUser Long userId) {
        PostResponse response = postService.createPost(request, userId);
        return ApiResponse.success(response);
    }
    
    @GetMapping("/posts")
    public ApiResponse<Page<PostResponse>> getPosts(
            @RequestParam(required = false) PostType postType,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PostResponse> posts = postService.getPosts(postType, keyword, page, size);
        return ApiResponse.success(posts);
    }
    
    @GetMapping("/posts/{postId}")
    public ApiResponse<PostResponse> getPostById(@PathVariable Long postId) {
        PostResponse post = postService.getPostById(postId);
        return ApiResponse.success(post);
    }
    
    @GetMapping("/posts/my")
    public ApiResponse<List<PostResponse>> getMyPosts(@CurrentUser Long userId) {
        List<PostResponse> posts = postService.getUserPosts(userId);
        return ApiResponse.success(posts);
    }
    
    @DeleteMapping("/posts/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId, @CurrentUser Long userId) {
        postService.deletePost(postId, userId);
        return ApiResponse.success(null);
    }
} 