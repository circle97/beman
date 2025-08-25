package com.beman.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.mapper.PostMapper;
import com.beman.mapper.UserMapper;
import com.beman.model.Post;
import com.beman.model.User;
import com.beman.model.dto.PostCreateDTO;
import com.beman.model.dto.PostQueryDTO;
import com.beman.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostMapper postMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private PostServiceImpl postService;

    private User testUser;
    private Post testPost;
    private PostCreateDTO testPostCreateDTO;
    private PostQueryDTO testPostQueryDTO;

    @BeforeEach
    void setUp() {
        // 设置测试数据
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setNickname("测试用户");

        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("测试帖子");
        testPost.setContent("测试内容");
        testPost.setAuthorId(1L);
        testPost.setAuthorName("测试用户");
        testPost.setTags(Arrays.asList("测试", "示例"));
        testPost.setLikeCount(0);
        testPost.setCommentCount(0);
        testPost.setViewCount(0);
        testPost.setIsAnonymous(false);
        testPost.setStatus(1);
        testPost.setCreateTime(LocalDateTime.now());
        testPost.setUpdateTime(LocalDateTime.now());

        testPostCreateDTO = new PostCreateDTO();
        testPostCreateDTO.setTitle("测试帖子");
        testPostCreateDTO.setContent("测试内容");
        testPostCreateDTO.setTags(Arrays.asList("测试", "示例"));
        testPostCreateDTO.setIsAnonymous(false);

        testPostQueryDTO = new PostQueryDTO();
        testPostQueryDTO.setPage(1);
        testPostQueryDTO.setSize(10);
    }

    @Test
    void testCreatePost_Success() {
        // 准备测试数据
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(postMapper.insert(any(Post.class))).thenReturn(1);

        // 执行测试
        Post result = postService.createPost(testPostCreateDTO, 1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(testPostCreateDTO.getTitle(), result.getTitle());
        assertEquals(testPostCreateDTO.getContent(), result.getContent());
        assertEquals(testPostCreateDTO.getTags(), result.getTags());
        assertEquals(testPostCreateDTO.getIsAnonymous(), result.getIsAnonymous());
        assertEquals(1L, result.getAuthorId());
        assertEquals(testUser.getNickname(), result.getAuthorName());
        assertEquals(1, result.getStatus());
        assertNotNull(result.getCreateTime());
        assertNotNull(result.getUpdateTime());

        // 验证方法调用
        verify(userMapper).selectById(1L);
        verify(postMapper).insert(any(Post.class));
    }

    @Test
    void testCreatePost_UserNotFound() {
        // 准备测试数据
        when(userMapper.selectById(1L)).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            postService.createPost(testPostCreateDTO, 1L);
        });

        // 验证方法调用
        verify(userMapper).selectById(1L);
        verify(postMapper, never()).insert(any(Post.class));
    }

    @Test
    void testGetPostPage_Success() {
        // 准备测试数据
        List<Post> posts = Arrays.asList(testPost);
        Page<Post> page = new Page<>(1, 10);
        page.setRecords(posts);
        page.setTotal(1);

        when(postMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(page);

        // 执行测试
        IPage<Post> result = postService.getPostPage(testPostQueryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        assertEquals(testPost.getTitle(), result.getRecords().get(0).getTitle());

        // 验证方法调用
        verify(postMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void testGetPostPage_EmptyResult() {
        // 准备测试数据
        Page<Post> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList());
        page.setTotal(0);

        when(postMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(page);

        // 执行测试
        IPage<Post> result = postService.getPostPage(testPostQueryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertEquals(0, result.getRecords().size());

        // 验证方法调用
        verify(postMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void testGetPostById_Success() {
        // 准备测试数据
        when(postMapper.selectById(1L)).thenReturn(testPost);

        // 执行测试
        Optional<Post> result = postService.getPostById(1L);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals(testPost.getTitle(), result.get().getTitle());
        assertEquals(testPost.getContent(), result.get().getContent());

        // 验证方法调用
        verify(postMapper).selectById(1L);
    }

    @Test
    void testGetPostById_NotFound() {
        // 准备测试数据
        when(postMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        Optional<Post> result = postService.getPostById(1L);

        // 验证结果
        assertFalse(result.isPresent());

        // 验证方法调用
        verify(postMapper).selectById(1L);
    }

    @Test
    void testUpdatePost_Success() {
        // 准备测试数据
        Post updatePost = new Post();
        updatePost.setId(1L);
        updatePost.setTitle("更新后的帖子");
        updatePost.setContent("更新后的内容");

        when(postMapper.selectById(1L)).thenReturn(testPost);
        when(postMapper.updateById(any(Post.class))).thenReturn(1);

        // 执行测试
        boolean result = postService.updatePost(1L, updatePost, 1L);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(postMapper).selectById(1L);
        verify(postMapper).updateById(any(Post.class));
    }

    @Test
    void testUpdatePost_NotFound() {
        // 准备测试数据
        Post updatePost = new Post();
        updatePost.setId(1L);
        updatePost.setTitle("更新后的帖子");

        when(postMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        boolean result = postService.updatePost(1L, updatePost, 1L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(postMapper).selectById(1L);
        verify(postMapper, never()).updateById(any(Post.class));
    }

    @Test
    void testUpdatePost_NotOwner() {
        // 准备测试数据
        Post updatePost = new Post();
        updatePost.setId(1L);
        updatePost.setTitle("更新后的帖子");

        when(postMapper.selectById(1L)).thenReturn(testPost);

        // 执行测试
        boolean result = postService.updatePost(1L, updatePost, 2L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(postMapper).selectById(1L);
        verify(postMapper, never()).updateById(any(Post.class));
    }

    @Test
    void testDeletePost_Success() {
        // 准备测试数据
        when(postMapper.selectById(1L)).thenReturn(testPost);
        when(postMapper.deleteById(1L)).thenReturn(1);

        // 执行测试
        boolean result = postService.deletePost(1L, 1L);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(postMapper).selectById(1L);
        verify(postMapper).deleteById(1L);
    }

    @Test
    void testDeletePost_NotFound() {
        // 准备测试数据
        when(postMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        boolean result = postService.deletePost(1L, 1L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(postMapper).selectById(1L);
        verify(postMapper, never()).deleteById(anyLong());
    }

    @Test
    void testDeletePost_NotOwner() {
        // 准备测试数据
        when(postMapper.selectById(1L)).thenReturn(testPost);

        // 执行测试
        boolean result = postService.deletePost(1L, 2L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(postMapper).selectById(1L);
        verify(postMapper, never()).deleteById(anyLong());
    }

    @Test
    void testLikePost_Success() {
        // 准备测试数据
        when(postMapper.selectById(1L)).thenReturn(testPost);
        when(postMapper.updateById(any(Post.class))).thenReturn(1);

        // 执行测试
        boolean result = postService.likePost(1L, 1L);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(postMapper).selectById(1L);
        verify(postMapper).updateById(any(Post.class));
    }

    @Test
    void testLikePost_NotFound() {
        // 准备测试数据
        when(postMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        boolean result = postService.likePost(1L, 1L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(postMapper).selectById(1L);
        verify(postMapper, never()).updateById(any(Post.class));
    }

    @Test
    void testUnlikePost_Success() {
        // 准备测试数据
        testPost.setLikeCount(1);
        when(postMapper.selectById(1L)).thenReturn(testPost);
        when(postMapper.updateById(any(Post.class))).thenReturn(1);

        // 执行测试
        boolean result = postService.unlikePost(1L, 1L);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(postMapper).selectById(1L);
        verify(postMapper).updateById(any(Post.class));
    }

    @Test
    void testUnlikePost_NotFound() {
        // 准备测试数据
        when(postMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        boolean result = postService.unlikePost(1L, 1L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(postMapper).selectById(1L);
        verify(postMapper, never()).updateById(any(Post.class));
    }

    @Test
    void testGetPostsByUser_Success() {
        // 准备测试数据
        List<Post> posts = Arrays.asList(testPost);
        when(postMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(posts);

        // 执行测试
        List<Post> result = postService.getPostsByUser(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPost.getTitle(), result.get(0).getTitle());

        // 验证方法调用
        verify(postMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    void testGetPostsByUser_Empty() {
        // 准备测试数据
        when(postMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Arrays.asList());

        // 执行测试
        List<Post> result = postService.getPostsByUser(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.size());

        // 验证方法调用
        verify(postMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    void testGetHotPosts_Success() {
        // 准备测试数据
        List<Post> posts = Arrays.asList(testPost);
        when(postMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(posts);

        // 执行测试
        List<Post> result = postService.getHotPosts(10);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPost.getTitle(), result.get(0).getTitle());

        // 验证方法调用
        verify(postMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    void testGetPostsByTag_Success() {
        // 准备测试数据
        List<Post> posts = Arrays.asList(testPost);
        when(postMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(posts);

        // 执行测试
        List<Post> result = postService.getPostsByTag("测试");

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPost.getTitle(), result.get(0).getTitle());

        // 验证方法调用
        verify(postMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    void testSearchPosts_Success() {
        // 准备测试数据
        List<Post> posts = Arrays.asList(testPost);
        when(postMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(posts);

        // 执行测试
        List<Post> result = postService.searchPosts("测试");

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPost.getTitle(), result.get(0).getTitle());

        // 验证方法调用
        verify(postMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    void testGetPostCount_Success() {
        // 准备测试数据
        when(postMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(5L);

        // 执行测试
        int result = postService.getPostCount();

        // 验证结果
        assertEquals(5, result);

        // 验证方法调用
        verify(postMapper).selectCount(any(LambdaQueryWrapper.class));
    }

    @Test
    void testGetPostCount_Zero() {
        // 准备测试数据
        when(postMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

        // 执行测试
        int result = postService.getPostCount();

        // 验证结果
        assertEquals(0, result);

        // 验证方法调用
        verify(postMapper).selectCount(any(LambdaQueryWrapper.class));
    }
}
