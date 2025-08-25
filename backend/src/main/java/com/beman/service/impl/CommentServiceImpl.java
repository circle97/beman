package com.beman.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.mapper.CommentMapper;
import com.beman.model.Comment;
import com.beman.model.User;
import com.beman.model.dto.CommentCreateDTO;
import com.beman.model.dto.CommentQueryDTO;
import com.beman.model.vo.CommentVO;
import com.beman.service.CommentService;
import com.beman.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论服务实现类
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final UserService userService;

    @Override
    @Transactional
    public Comment createComment(CommentCreateDTO createDTO) {
        // 获取当前用户信息
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.findById(userId);
        
        // 创建评论实体
        Comment comment = new Comment();
        BeanUtils.copyProperties(createDTO, comment);
        comment.setAuthorId(userId);
        comment.setAuthorName(user.getUsername());
        comment.setAuthorAvatar(user.getAvatar());
        comment.setLikeCount(0);
        comment.setStatus(0);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        
        // 保存评论
        commentMapper.insert(comment);
        
        return comment;
    }

    @Override
    public IPage<CommentVO> getCommentPage(CommentQueryDTO queryDTO) {
        // 创建分页对象
        Page<CommentVO> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        
        // 查询评论列表
        IPage<CommentVO> commentPage = commentMapper.selectCommentPage(
            page, queryDTO.getPostId(), queryDTO.getParentId(), queryDTO.getSortType()
        );
        
        // 获取当前用户ID
        Long currentUserId = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
        
        // 处理评论数据
        List<CommentVO> records = commentPage.getRecords().stream()
            .map(comment -> {
                // 设置是否已点赞
                if (currentUserId != null) {
                    comment.setIsLiked(commentMapper.selectIsLiked(comment.getId(), currentUserId));
                }
                
                // 设置子评论数量
                comment.setChildrenCount(commentMapper.selectChildrenCount(comment.getId()));
                
                // 如果是顶级评论，加载子评论
                if (comment.getParentId() == null && comment.getChildrenCount() > 0) {
                    List<CommentVO> children = commentMapper.selectChildren(comment.getId());
                    children.forEach(child -> {
                        if (currentUserId != null) {
                            child.setIsLiked(commentMapper.selectIsLiked(child.getId(), currentUserId));
                        }
                    });
                    comment.setChildren(children);
                }
                
                return comment;
            })
            .collect(Collectors.toList());
        
        commentPage.setRecords(records);
        return commentPage;
    }

    @Override
    public CommentVO getCommentDetail(Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);
        
        // 设置是否已点赞
        if (StpUtil.isLogin()) {
            Long currentUserId = StpUtil.getLoginIdAsLong();
            commentVO.setIsLiked(commentMapper.selectIsLiked(id, currentUserId));
        }
        
        return commentVO;
    }

    @Override
    @Transactional
    public void likeComment(Long id) {
        // 检查评论是否存在
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        
        // TODO: 检查用户是否已点赞，避免重复点赞
        // 这里需要创建评论点赞记录表来记录用户点赞状态
        
        // 更新点赞数
        commentMapper.updateLikeCount(id, 1);
    }

    @Override
    @Transactional
    public void unlikeComment(Long id) {
        // 检查评论是否存在
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        
        // TODO: 检查用户是否已点赞，只有已点赞才能取消
        // 这里需要创建评论点赞记录表来记录用户点赞状态
        
        // 更新点赞数
        commentMapper.updateLikeCount(id, -1);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        // 检查评论是否存在
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        
        // 检查权限：只有评论作者或管理员才能删除
        Long userId = StpUtil.getLoginIdAsLong();
        if (!comment.getAuthorId().equals(userId)) {
            // TODO: 检查是否为管理员
            throw new RuntimeException("无权限删除此评论");
        }
        
        // 逻辑删除评论
        commentMapper.deleteById(id);
    }

    @Override
    public Integer getCommentCount(Long postId) {
        // 统计帖子的评论数量（包括子评论）
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getPostId, postId);
        return Math.toIntExact(commentMapper.selectCount(wrapper));
    }
}
