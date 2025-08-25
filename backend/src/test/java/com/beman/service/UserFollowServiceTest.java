package com.beman.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.mapper.UserFollowMapper;
import com.beman.mapper.UserMapper;
import com.beman.model.User;
import com.beman.model.UserFollow;
import com.beman.model.dto.UserFollowDTO;
import com.beman.model.dto.UserFollowQueryDTO;
import com.beman.model.vo.UserFollowVO;
import com.beman.service.impl.UserFollowServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFollowServiceTest {

    @Mock
    private UserFollowMapper userFollowMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserFollowServiceImpl userFollowService;

    private User testUser;
    private User targetUser;
    private UserFollow testUserFollow;
    private UserFollowDTO testUserFollowDTO;
    private UserFollowQueryDTO testUserFollowQueryDTO;

    @BeforeEach
    void setUp() {
        // 设置测试数据
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setNickname("测试用户");

        targetUser = new User();
        targetUser.setId(2L);
        targetUser.setUsername("targetuser");
        targetUser.setNickname("目标用户");

        testUserFollow = new UserFollow();
        testUserFollow.setId(1L);
        testUserFollow.setFollowerId(1L);
        testUserFollow.setFollowingId(2L);
        testUserFollow.setCreateTime(LocalDateTime.now());

        testUserFollowDTO = new UserFollowDTO();
        testUserFollowDTO.setFollowingId(2L);

        testUserFollowQueryDTO = new UserFollowQueryDTO();
        testUserFollowQueryDTO.setUserId(1L);
        testUserFollowQueryDTO.setPage(1);
        testUserFollowQueryDTO.setSize(10);
    }

    @Test
    void testFollowUser_Success() {
        // 准备测试数据
        when(userService.findById(2L)).thenReturn(targetUser);
        when(userFollowMapper.selectFollowRelation(1L, 2L)).thenReturn(null);
        when(userFollowMapper.insert(any(UserFollow.class))).thenReturn(1);

        // 执行测试
        UserFollow result = userFollowService.followUser(testUserFollowDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getFollowerId());
        assertEquals(2L, result.getFollowingId());

        // 验证方法调用
        verify(userService).findById(2L);
        verify(userFollowMapper).selectFollowRelation(1L, 2L);
        verify(userFollowMapper).insert(any(UserFollow.class));
    }

    @Test
    void testFollowUser_AlreadyFollowing() {
        // 准备测试数据
        when(userService.findById(2L)).thenReturn(targetUser);
        testUserFollow.setStatus(0);
        when(userFollowMapper.selectFollowRelation(1L, 2L)).thenReturn(testUserFollow);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            userFollowService.followUser(testUserFollowDTO);
        });

        // 验证方法调用
        verify(userService).findById(2L);
        verify(userFollowMapper).selectFollowRelation(1L, 2L);
        verify(userFollowMapper, never()).insert(any(UserFollow.class));
    }

    @Test
    void testFollowUser_TargetUserNotFound() {
        // 准备测试数据
        when(userService.findById(2L)).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            userFollowService.followUser(testUserFollowDTO);
        });

        // 验证方法调用
        verify(userService).findById(2L);
        verify(userFollowMapper, never()).selectFollowRelation(anyLong(), anyLong());
        verify(userFollowMapper, never()).insert(any(UserFollow.class));
    }

    @Test
    void testFollowUser_SelfFollow() {
        // 准备测试数据
        testUserFollowDTO.setFollowingId(1L);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            userFollowService.followUser(testUserFollowDTO);
        });

        // 验证方法调用
        verify(userService, never()).findById(anyLong());
        verify(userFollowMapper, never()).selectFollowRelation(anyLong(), anyLong());
        verify(userFollowMapper, never()).insert(any(UserFollow.class));
    }

    @Test
    void testUnfollowUser_Success() {
        // 准备测试数据
        testUserFollow.setStatus(0);
        when(userFollowMapper.selectFollowRelation(1L, 2L)).thenReturn(testUserFollow);
        when(userFollowMapper.updateById(any(UserFollow.class))).thenReturn(1);

        // 执行测试
        assertDoesNotThrow(() -> {
            userFollowService.unfollowUser(2L);
        });

        // 验证方法调用
        verify(userFollowMapper).selectFollowRelation(1L, 2L);
        verify(userFollowMapper).updateById(any(UserFollow.class));
    }

    @Test
    void testUnfollowUser_NotFollowing() {
        // 准备测试数据
        when(userFollowMapper.selectFollowRelation(1L, 2L)).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            userFollowService.unfollowUser(2L);
        });

        // 验证方法调用
        verify(userFollowMapper).selectFollowRelation(1L, 2L);
        verify(userFollowMapper, never()).updateById(any(UserFollow.class));
    }

    @Test
    void testIsFollowing_True() {
        // 准备测试数据
        when(userFollowMapper.selectIsFollowing(1L, 2L)).thenReturn(true);

        // 执行测试
        Boolean result = userFollowService.isFollowing(2L);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(userFollowMapper).selectIsFollowing(1L, 2L);
    }

    @Test
    void testIsFollowing_False() {
        // 准备测试数据
        when(userFollowMapper.selectIsFollowing(1L, 2L)).thenReturn(false);

        // 执行测试
        Boolean result = userFollowService.isFollowing(2L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(userFollowMapper).selectIsFollowing(1L, 2L);
    }

    @Test
    void testGetFollowingPage_Success() {
        // 准备测试数据
        List<UserFollowVO> userFollows = Arrays.asList(new UserFollowVO());
        Page<UserFollowVO> page = new Page<>(1, 10);
        page.setRecords(userFollows);
        page.setTotal(1);

        when(userFollowMapper.selectFollowingPage(any(Page.class), eq(1L), any(), any()))
                .thenReturn(page);
        when(userFollowMapper.selectIsFollowing(anyLong(), anyLong())).thenReturn(true);

        // 执行测试
        IPage<UserFollowVO> result = userFollowService.getFollowingPage(testUserFollowQueryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());

        // 验证方法调用
        verify(userFollowMapper).selectFollowingPage(any(Page.class), eq(1L), any(), any());
        verify(userFollowMapper).selectIsFollowing(anyLong(), anyLong());
    }

    @Test
    void testGetFollowingPage_EmptyResult() {
        // 准备测试数据
        Page<UserFollowVO> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList());
        page.setTotal(0);

        when(userFollowMapper.selectFollowingPage(any(Page.class), eq(1L), any(), any()))
                .thenReturn(page);

        // 执行测试
        IPage<UserFollowVO> result = userFollowService.getFollowingPage(testUserFollowQueryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertEquals(0, result.getRecords().size());

        // 验证方法调用
        verify(userFollowMapper).selectFollowingPage(any(Page.class), eq(1L), any(), any());
        verify(userFollowMapper, never()).selectIsFollowing(anyLong(), anyLong());
    }

    @Test
    void testGetFollowersPage_Success() {
        // 准备测试数据
        List<UserFollowVO> userFollows = Arrays.asList(new UserFollowVO());
        Page<UserFollowVO> page = new Page<>(1, 10);
        page.setRecords(userFollows);
        page.setTotal(1);

        when(userFollowMapper.selectFollowersPage(any(Page.class), eq(1L), any(), any()))
                .thenReturn(page);
        when(userFollowMapper.selectIsFollowing(anyLong(), anyLong())).thenReturn(true);

        // 执行测试
        IPage<UserFollowVO> result = userFollowService.getFollowersPage(testUserFollowQueryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());

        // 验证方法调用
        verify(userFollowMapper).selectFollowersPage(any(Page.class), eq(1L), any(), any());
        verify(userFollowMapper).selectIsFollowing(anyLong(), anyLong());
    }

    @Test
    void testGetFollowersPage_EmptyResult() {
        // 准备测试数据
        Page<UserFollowVO> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList());
        page.setTotal(0);

        when(userFollowMapper.selectFollowersPage(any(Page.class), eq(1L), any(), any()))
                .thenReturn(page);

        // 执行测试
        IPage<UserFollowVO> result = userFollowService.getFollowersPage(testUserFollowQueryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertEquals(0, result.getRecords().size());

        // 验证方法调用
        verify(userFollowMapper).selectFollowersPage(any(Page.class), eq(1L), any(), any());
        verify(userFollowMapper, never()).selectIsFollowing(anyLong(), anyLong());
    }

    @Test
    void testGetFollowingCount_Success() {
        // 准备测试数据
        when(userFollowMapper.selectFollowingCount(1L)).thenReturn(5);

        // 执行测试
        Integer result = userFollowService.getFollowingCount(1L);

        // 验证结果
        assertEquals(5, result);

        // 验证方法调用
        verify(userFollowMapper).selectFollowingCount(1L);
    }

    @Test
    void testGetFollowingCount_Zero() {
        // 准备测试数据
        when(userFollowMapper.selectFollowingCount(1L)).thenReturn(0);

        // 执行测试
        Integer result = userFollowService.getFollowingCount(1L);

        // 验证结果
        assertEquals(0, result);

        // 验证方法调用
        verify(userFollowMapper).selectFollowingCount(1L);
    }

    @Test
    void testGetFollowersCount_Success() {
        // 准备测试数据
        when(userFollowMapper.selectFollowersCount(1L)).thenReturn(3);

        // 执行测试
        Integer result = userFollowService.getFollowersCount(1L);

        // 验证结果
        assertEquals(3, result);

        // 验证方法调用
        verify(userFollowMapper).selectFollowersCount(1L);
    }

    @Test
    void testGetFollowersCount_Zero() {
        // 准备测试数据
        when(userFollowMapper.selectFollowersCount(1L)).thenReturn(0);

        // 执行测试
        Integer result = userFollowService.getFollowersCount(1L);

        // 验证结果
        assertEquals(0, result);

        // 验证方法调用
        verify(userFollowMapper).selectFollowersCount(1L);
    }

    @Test
    void testGetMutualFollowUsers_Success() {
        // 准备测试数据
        List<Long> mutualUserIds = Arrays.asList(2L);
        when(userFollowMapper.selectMutualFollowUserIds(1L)).thenReturn(mutualUserIds);
        when(userService.findById(2L)).thenReturn(targetUser);

        // 执行测试
        List<UserFollowVO> result = userFollowService.getMutualFollowUsers(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(targetUser.getNickname(), result.get(0).getNickname());

        // 验证方法调用
        verify(userFollowMapper).selectMutualFollowUserIds(1L);
        verify(userService).findById(2L);
    }

    @Test
    void testGetMutualFollows_Empty() {
        // 准备测试数据
        when(userFollowMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Arrays.asList());

        // 执行测试
        List<UserFollowVO> result = userFollowService.getMutualFollows(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.size());

        // 验证方法调用
        verify(userFollowMapper).selectList(any(LambdaQueryWrapper.class));
        verify(userMapper, never()).selectById(anyLong());
    }
}
