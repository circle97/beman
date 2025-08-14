package com.beman.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.mapper.PostMapper;
import com.beman.mapper.UserMapper;
import com.beman.model.Post;
import com.beman.model.User;
import com.beman.model.dto.PostCreateDTO;
import com.beman.model.dto.PostQueryDTO;
import com.beman.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子服务实现类
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public Post createPost(PostCreateDTO createDTO) {
        // 获取当前用户
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 创建帖子
        Post post = new Post();
        post.setUserId(userId);
        post.setTitle(createDTO.getTitle());
        post.setContent(createDTO.getContent());
        post.setContentType(createDTO.getContentType());
        post.setVoiceUrl(createDTO.getVoiceUrl());
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setStatus(1); // 正常状态
        post.setIsTop(0); // 默认不置顶

        // 处理标签
        if (createDTO.getTags() != null && !createDTO.getTags().isEmpty()) {
            try {
                post.setTags(objectMapper.writeValueAsString(createDTO.getTags()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("标签格式错误");
            }
        }

        // 处理匿名发布
        if (Boolean.TRUE.equals(createDTO.getIsAnonymous())) {
            post.setAnonymousId(user.getAnonymousId());
        }

        // 保存帖子
        postMapper.insert(post);

        // 设置作者信息
        post.setAuthorName(Boolean.TRUE.equals(createDTO.getIsAnonymous()) ? "匿名用户" : user.getNickname());
        post.setAuthorAvatar(user.getAvatar());

        return post;
    }

    @Override
    public IPage<Post> getPostPage(PostQueryDTO queryDTO) {
        // 创建分页对象
        Page<Post> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());

        // 查询帖子列表
        IPage<Post> postPage = postMapper.selectPostPage(page, queryDTO.getTags(), 
                                                       queryDTO.getKeyword(), queryDTO.getContentType());

        // 获取当前用户ID（用于判断是否点赞）
        Long currentUserId = null;
        if (StpUtil.isLogin()) {
            currentUserId = StpUtil.getLoginIdAsLong();
        }

        // 处理帖子数据
        postPage.getRecords().forEach(post -> {
            // 解析标签
            if (StringUtils.hasText(post.getTags())) {
                try {
                    List<String> tags = objectMapper.readValue(post.getTags(), new TypeReference<List<String>>() {});
                    // 这里可以设置到post对象中，但需要添加字段
                } catch (JsonProcessingException e) {
                    // 忽略解析错误
                }
            }

            // 设置作者信息
            if (post.getAnonymousId() != null) {
                post.setAuthorName("匿名用户");
            } else {
                User author = userMapper.selectById(post.getUserId());
                if (author != null) {
                    post.setAuthorName(author.getNickname());
                    post.setAuthorAvatar(author.getAvatar());
                }
            }

            // TODO: 设置是否点赞状态（需要查询点赞表）
            post.setIsLiked(false);
        });

        return postPage;
    }

    @Override
    public Post getPostDetail(Long id) {
        Long currentUserId = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
        Post post = postMapper.selectPostDetail(id, currentUserId);
        
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }

        // 增加浏览次数
        postMapper.updateViewCount(id);

        // 设置作者信息
        if (post.getAnonymousId() != null) {
            post.setAuthorName("匿名用户");
        } else {
            User author = userMapper.selectById(post.getUserId());
            if (author != null) {
                post.setAuthorName(author.getNickname());
                post.setAuthorAvatar(author.getAvatar());
            }
        }

        return post;
    }

    @Override
    @Transactional
    public void likePost(Long postId) {
        // 检查帖子是否存在
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }

        // TODO: 检查用户是否已点赞（需要点赞表）
        // 这里简化处理，直接增加点赞数
        postMapper.updateLikeCount(postId, 1);
    }

    @Override
    @Transactional
    public void unlikePost(Long postId) {
        // 检查帖子是否存在
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }

        // TODO: 检查用户是否已点赞（需要点赞表）
        // 这里简化处理，直接减少点赞数
        postMapper.updateLikeCount(postId, -1);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        // 获取当前用户
        Long userId = StpUtil.getLoginIdAsLong();
        
        // 检查帖子是否存在且属于当前用户
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除此帖子");
        }

        // 逻辑删除帖子
        postMapper.deleteById(postId);
    }

    @Override
    public List<String> getHotTags() {
        // TODO: 实现热门标签查询
        // 这里返回一些预设的热门标签
        return Arrays.asList("挽回经验", "冷战化解", "沟通技巧", "自我提升", "关系经营", "情绪管理");
    }
}
