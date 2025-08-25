package com.beman.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beman.model.Comment;
import com.beman.model.dto.CommentCreateDTO;
import com.beman.model.dto.CommentQueryDTO;
import com.beman.model.vo.CommentVO;
import com.beman.model.vo.Result;
import com.beman.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    /**
     * 创建评论
     */
    @PostMapping
    public Result<Comment> createComment(@Valid @RequestBody CommentCreateDTO createDTO) {
        try {
            Comment comment = commentService.createComment(createDTO);
            return Result.success("评论发布成功", comment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询评论列表
     */
    @GetMapping("/page")
    public Result<IPage<CommentVO>> getCommentPage(CommentQueryDTO queryDTO) {
        try {
            IPage<CommentVO> commentPage = commentService.getCommentPage(queryDTO);
            return Result.success(commentPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取评论详情
     */
    @GetMapping("/{id}")
    public Result<CommentVO> getCommentDetail(@PathVariable Long id) {
        try {
            CommentVO comment = commentService.getCommentDetail(id);
            return Result.success(comment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 点赞评论
     */
    @PostMapping("/{id}/like")
    public Result<String> likeComment(@PathVariable Long id) {
        try {
            commentService.likeComment(id);
            return Result.success("点赞成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消点赞评论
     */
    @DeleteMapping("/{id}/like")
    public Result<String> unlikeComment(@PathVariable Long id) {
        try {
            commentService.unlikeComment(id);
            return Result.success("取消点赞成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取帖子的评论数量
     */
    @GetMapping("/count/{postId}")
    public Result<Integer> getCommentCount(@PathVariable Long postId) {
        try {
            Integer count = commentService.getCommentCount(postId);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
