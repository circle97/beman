package com.beman.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.mapper.CommentMapper;
import com.beman.mapper.PostMapper;
import com.beman.mapper.UserMapper;
import com.beman.model.Comment;
import com.beman.model.Post;
import com.beman.model.User;
import com.beman.model.dto.CommentCreateDTO;
import com.beman.model.dto.CommentQueryDTO;
import com.beman.model.vo.CommentVO;
import com.beman.service.impl.CommentServiceImpl;
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
class CommentServiceTest {

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private PostMapper postMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment testComment;
    private CommentCreateDTO testCommentCreateDTO;
    private CommentQueryDTO testCommentQueryDTO;
    private Post testPost;
    private User testUser;

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

        testComment = new Comment();
        testComment.setId(1L);
        testComment.setPostId(1L);
        testComment.setUserId(1L);
        testComment.setContent("测试评论");
        testComment.setCreateTime(LocalDateTime.now());
        testComment.setUpdateTime(LocalDateTime.now());

        testCommentCreateDTO = new CommentCreateDTO();
        testCommentCreateDTO.setPostId(1L);
        testCommentCreateDTO.setContent("测试评论内容");
        testCommentCreateDTO.setParentId(null);

        testCommentQueryDTO = new CommentQueryDTO();
        testCommentQueryDTO.setPostId(1L);
        testCommentQueryDTO.setPage(1);
        testCommentQueryDTO.setSize(10);
    }

    @Test
    void testCreateComment_Success() {
        // 准备测试数据
        when(postMapper.selectById(1L)).thenReturn(testPost);
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(commentMapper.insert(any(Comment.class))).thenReturn(1);

        // 执行测试
        Comment result = commentService.createComment(testCommentCreateDTO, 1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(testCommentCreateDTO.getPostId(), result.getPostId());
        assertEquals(testCommentCreateDTO.getContent(), result.getContent());
        assertEquals(1L, result.getUserId());
        assertNotNull(result.getCreateTime());
        assertNotNull(result.getUpdateTime());

        // 验证方法调用
        verify(postMapper).selectById(1L);
        verify(userMapper).selectById(1L);
        verify(commentMapper).insert(any(Comment.class));
    }

    @Test
    void testCreateComment_PostNotFound() {
        // 准备测试数据
        when(postMapper.selectById(1L)).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            commentService.createComment(testCommentCreateDTO, 1L);
        });

        // 验证方法调用
        verify(postMapper).selectById(1L);
        verify(commentMapper, never()).insert(any(Comment.class));
    }

    @Test
    void testCreateComment_UserNotFound() {
        // 准备测试数据
        when(postMapper.selectById(1L)).thenReturn(testPost);
        when(userMapper.selectById(1L)).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            commentService.createComment(testCommentCreateDTO, 1L);
        });

        // 验证方法调用
        verify(postMapper).selectById(1L);
        verify(userMapper).selectById(1L);
        verify(commentMapper, never()).insert(any(Comment.class));
    }

    @Test
    void testGetCommentPage_Success() {
        // 准备测试数据
        List<Comment> comments = Arrays.asList(testComment);
        Page<Comment> page = new Page<>(1, 10);
        page.setRecords(comments);
        page.setTotal(1);

        when(commentMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(page);
        when(userMapper.selectById(1L)).thenReturn(testUser);

        // 执行测试
        IPage<CommentVO> result = commentService.getCommentPage(testCommentQueryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        assertEquals(testComment.getContent(), result.getRecords().get(0).getContent());
        assertEquals(testUser.getNickname(), result.getRecords().get(0).getAuthorName());

        // 验证方法调用
        verify(commentMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
        verify(userMapper).selectById(1L);
    }

    @Test
    void testGetCommentPage_EmptyResult() {
        // 准备测试数据
        Page<Comment> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList());
        page.setTotal(0);

        when(commentMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(page);

        // 执行测试
        IPage<CommentVO> result = commentService.getCommentPage(testCommentQueryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertEquals(0, result.getRecords().size());

        // 验证方法调用
        verify(commentMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
        verify(userMapper, never()).selectById(anyLong());
    }

    @Test
    void testGetCommentById_Success() {
        // 准备测试数据
        when(commentMapper.selectById(1L)).thenReturn(testComment);
        when(userMapper.selectById(1L)).thenReturn(testUser);

        // 执行测试
        Optional<CommentVO> result = commentService.getCommentById(1L);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals(testComment.getContent(), result.get().getContent());
        assertEquals(testUser.getNickname(), result.get().getAuthorName());

        // 验证方法调用
        verify(commentMapper).selectById(1L);
        verify(userMapper).selectById(1L);
    }

    @Test
    void testGetCommentById_NotFound() {
        // 准备测试数据
        when(commentMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        Optional<CommentVO> result = commentService.getCommentById(1L);

        // 验证结果
        assertFalse(result.isPresent());

        // 验证方法调用
        verify(commentMapper).selectById(1L);
        verify(userMapper, never()).selectById(anyLong());
    }

    @Test
    void testUpdateComment_Success() {
        // 准备测试数据
        Comment updateComment = new Comment();
        updateComment.setId(1L);
        updateComment.setContent("更新后的评论");

        when(commentMapper.selectById(1L)).thenReturn(testComment);
        when(commentMapper.updateById(any(Comment.class))).thenReturn(1);

        // 执行测试
        boolean result = commentService.updateComment(1L, updateComment, 1L);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(commentMapper).selectById(1L);
        verify(commentMapper).updateById(any(Comment.class));
    }

    @Test
    void testUpdateComment_NotFound() {
        // 准备测试数据
        Comment updateComment = new Comment();
        updateComment.setId(1L);
        updateComment.setContent("更新后的评论");

        when(commentMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        boolean result = commentService.updateComment(1L, updateComment, 1L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(commentMapper).selectById(1L);
        verify(commentMapper, never()).updateById(any(Comment.class));
    }

    @Test
    void testUpdateComment_NotOwner() {
        // 准备测试数据
        Comment updateComment = new Comment();
        updateComment.setId(1L);
        updateComment.setContent("更新后的评论");

        when(commentMapper.selectById(1L)).thenReturn(testComment);

        // 执行测试
        boolean result = commentService.updateComment(1L, updateComment, 2L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(commentMapper).selectById(1L);
        verify(commentMapper, never()).updateById(any(Comment.class));
    }

    @Test
    void testDeleteComment_Success() {
        // 准备测试数据
        when(commentMapper.selectById(1L)).thenReturn(testComment);
        when(commentMapper.deleteById(1L)).thenReturn(1);

        // 执行测试
        boolean result = commentService.deleteComment(1L, 1L);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(commentMapper).selectById(1L);
        verify(commentMapper).deleteById(1L);
    }

    @Test
    void testDeleteComment_NotFound() {
        // 准备测试数据
        when(commentMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        boolean result = commentService.deleteComment(1L, 1L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(commentMapper).selectById(1L);
        verify(commentMapper, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteComment_NotOwner() {
        // 准备测试数据
        when(commentMapper.selectById(1L)).thenReturn(testComment);

        // 执行测试
        boolean result = commentService.deleteComment(1L, 2L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(commentMapper).selectById(1L);
        verify(commentMapper, never()).deleteById(anyLong());
    }

    @Test
    void testGetCommentCount_Success() {
        // 准备测试数据
        when(commentMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(5L);

        // 执行测试
        int result = commentService.getCommentCount(1L);

        // 验证结果
        assertEquals(5, result);

        // 验证方法调用
        verify(commentMapper).selectCount(any(LambdaQueryWrapper.class));
    }

    @Test
    void testGetCommentCount_Zero() {
        // 准备测试数据
        when(commentMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

        // 执行测试
        int result = commentService.getCommentCount(1L);

        // 验证结果
        assertEquals(0, result);

        // 验证方法调用
        verify(commentMapper).selectCount(any(LambdaQueryWrapper.class));
    }

    @Test
    void testGetCommentsByPostId_Success() {
        // 准备测试数据
        List<Comment> comments = Arrays.asList(testComment);
        when(commentMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(comments);
        when(userMapper.selectById(1L)).thenReturn(testUser);

        // 执行测试
        List<CommentVO> result = commentService.getCommentsByPostId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testComment.getContent(), result.get(0).getContent());
        assertEquals(testUser.getNickname(), result.get(0).getAuthorName());

        // 验证方法调用
        verify(commentMapper).selectList(any(LambdaQueryWrapper.class));
        verify(userMapper).selectById(1L);
    }

    @Test
    void testGetCommentsByPostId_Empty() {
        // 准备测试数据
        when(commentMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Arrays.asList());

        // 执行测试
        List<CommentVO> result = commentService.getCommentsByPostId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.size());

        // 验证方法调用
        verify(commentMapper).selectList(any(LambdaQueryWrapper.class));
        verify(userMapper, never()).selectById(anyLong());
    }
}
