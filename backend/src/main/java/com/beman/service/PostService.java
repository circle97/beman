package com.beman.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beman.model.Post;
import com.beman.model.dto.PostCreateDTO;
import com.beman.model.dto.PostQueryDTO;

/**
 * 帖子服务接口
 */
public interface PostService {

    /**
     * 创建帖子
     */
    Post createPost(PostCreateDTO createDTO);

    /**
     * 分页查询帖子列表
     */
    IPage<Post> getPostPage(PostQueryDTO queryDTO);

    /**
     * 根据ID获取帖子详情
     */
    Post getPostDetail(Long id);

    /**
     * 点赞帖子
     */
    void likePost(Long postId);

    /**
     * 取消点赞
     */
    void unlikePost(Long postId);

    /**
     * 删除帖子
     */
    void deletePost(Long postId);

    /**
     * 获取热门标签
     */
    java.util.List<String> getHotTags();
}
