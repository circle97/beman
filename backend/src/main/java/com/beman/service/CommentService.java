package com.beman.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beman.model.Comment;
import com.beman.model.dto.CommentCreateDTO;
import com.beman.model.dto.CommentQueryDTO;
import com.beman.model.vo.CommentVO;

/**
 * 评论服务接口
 */
public interface CommentService {

    /**
     * 创建评论
     */
    Comment createComment(CommentCreateDTO createDTO);

    /**
     * 分页查询评论列表
     */
    IPage<CommentVO> getCommentPage(CommentQueryDTO queryDTO);

    /**
     * 获取评论详情
     */
    CommentVO getCommentDetail(Long id);

    /**
     * 点赞评论
     */
    void likeComment(Long id);

    /**
     * 取消点赞评论
     */
    void unlikeComment(Long id);

    /**
     * 删除评论
     */
    void deleteComment(Long id);

    /**
     * 获取帖子的评论数量
     */
    Integer getCommentCount(Long postId);
}
