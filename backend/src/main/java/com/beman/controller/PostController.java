package com.beman.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beman.model.Post;
import com.beman.model.dto.PostCreateDTO;
import com.beman.model.dto.PostQueryDTO;
import com.beman.model.vo.Result;
import com.beman.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 帖子控制器
 */
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;

    /**
     * 创建帖子
     */
    @PostMapping
    public Result<Post> createPost(@Valid @RequestBody PostCreateDTO createDTO) {
        try {
            Post post = postService.createPost(createDTO);
            return Result.success("发布成功", post);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询帖子列表
     */
    @GetMapping("/page")
    public Result<IPage<Post>> getPostPage(PostQueryDTO queryDTO) {
        try {
            IPage<Post> postPage = postService.getPostPage(queryDTO);
            return Result.success(postPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取帖子详情
     */
    @GetMapping("/{id}")
    public Result<Post> getPostDetail(@PathVariable Long id) {
        try {
            Post post = postService.getPostDetail(id);
            return Result.success(post);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 点赞帖子
     */
    @PostMapping("/{id}/like")
    public Result<Void> likePost(@PathVariable Long id) {
        try {
            postService.likePost(id);
            return Result.success("点赞成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消点赞
     */
    @DeleteMapping("/{id}/like")
    public Result<Void> unlikePost(@PathVariable Long id) {
        try {
            postService.unlikePost(id);
            return Result.success("取消点赞成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除帖子
     */
    @DeleteMapping("/{id}")
    public Result<Void> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取热门标签
     */
    @GetMapping("/hot-tags")
    public Result<List<String>> getHotTags() {
        try {
            List<String> hotTags = postService.getHotTags();
            return Result.success(hotTags);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
