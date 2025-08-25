package com.beman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.model.Comment;
import com.beman.model.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论Mapper接口
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 分页查询评论列表
     */
    IPage<CommentVO> selectCommentPage(Page<CommentVO> page, @Param("postId") Long postId, 
                                      @Param("parentId") Long parentId, @Param("sortType") Integer sortType);

    /**
     * 查询评论的子评论数量
     */
    Integer selectChildrenCount(@Param("commentId") Long commentId);

    /**
     * 查询评论的子评论列表
     */
    List<CommentVO> selectChildren(@Param("commentId") Long commentId);

    /**
     * 查询用户是否已点赞评论
     */
    Boolean selectIsLiked(@Param("commentId") Long commentId, @Param("userId") Long userId);

    /**
     * 更新评论点赞数
     */
    int updateLikeCount(@Param("commentId") Long commentId, @Param("increment") Integer increment);
}
