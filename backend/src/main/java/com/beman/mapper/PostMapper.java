package com.beman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.model.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子Mapper接口
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 分页查询帖子列表（包含作者信息）
     */
    IPage<Post> selectPostPage(Page<Post> page, @Param("tags") List<String> tags, 
                              @Param("keyword") String keyword, @Param("contentType") Integer contentType);

    /**
     * 根据ID查询帖子详情（包含作者信息）
     */
    Post selectPostDetail(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 更新帖子浏览次数
     */
    int updateViewCount(@Param("id") Long id);

    /**
     * 更新帖子点赞次数
     */
    int updateLikeCount(@Param("id") Long id, @Param("increment") int increment);

    /**
     * 更新帖子评论次数
     */
    int updateCommentCount(@Param("id") Long id, @Param("increment") int increment);
}
