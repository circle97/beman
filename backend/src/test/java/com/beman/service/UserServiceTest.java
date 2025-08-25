package com.beman.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.mapper.UserMapper;
import com.beman.model.User;
import com.beman.model.dto.UserLoginDTO;
import com.beman.model.dto.UserRegisterDTO;
import com.beman.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserRegisterDTO testUserRegisterDTO;
    private UserLoginDTO testUserLoginDTO;

    @BeforeEach
    void setUp() {
        // 设置测试数据
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedpassword");
        testUser.setNickname("测试用户");
        testUser.setEmail("test@example.com");
        testUser.setPhone("13800138000");
        testUser.setAvatar("avatar.jpg");
        testUser.setStatus(1);
        testUser.setCreateTime(LocalDateTime.now());
        testUser.setUpdateTime(LocalDateTime.now());
        testUser.setLastLoginTime(LocalDateTime.now());

        testUserRegisterDTO = new UserRegisterDTO();
        testUserRegisterDTO.setUsername("newuser");
        testUserRegisterDTO.setPassword("password123");
        testUserRegisterDTO.setNickname("新用户");
        testUserRegisterDTO.setEmail("newuser@example.com");
        testUserRegisterDTO.setPhone("13900139000");

        testUserLoginDTO = new UserLoginDTO();
        testUserLoginDTO.setUsername("testuser");
        testUserLoginDTO.setPassword("password123");
    }

    @Test
    void testRegister_Success() {
        // 准备测试数据
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encodedpassword");
        when(userMapper.insert(any(User.class))).thenReturn(1);

        // 执行测试
        User result = userService.register(testUserRegisterDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(testUserRegisterDTO.getUsername(), result.getUsername());
        assertEquals(testUserRegisterDTO.getNickname(), result.getNickname());
        assertEquals(testUserRegisterDTO.getEmail(), result.getEmail());
        assertEquals(testUserRegisterDTO.getPhone(), result.getPhone());
        assertEquals("encodedpassword", result.getPassword());
        assertEquals(1, result.getStatus());
        assertNotNull(result.getCreateTime());
        assertNotNull(result.getUpdateTime());

        // 验证方法调用
        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(passwordEncoder).encode("password123");
        verify(userMapper).insert(any(User.class));
    }

    @Test
    void testRegister_UsernameExists() {
        // 准备测试数据
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            userService.register(testUserRegisterDTO);
        });

        // 验证方法调用
        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(passwordEncoder, never()).encode(anyString());
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void testRegister_EmailExists() {
        // 准备测试数据
        when(userMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(null) // 用户名不存在
                .thenReturn(testUser); // 邮箱已存在

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            userService.register(testUserRegisterDTO);
        });

        // 验证方法调用
        verify(userMapper, times(2)).selectOne(any(LambdaQueryWrapper.class));
        verify(passwordEncoder, never()).encode(anyString());
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void testLogin_Success() {
        // 准备测试数据
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);
        when(passwordEncoder.matches("password123", "encodedpassword")).thenReturn(true);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // 执行测试
        User result = userService.login(testUserLoginDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getNickname(), result.getNickname());

        // 验证方法调用
        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(passwordEncoder).matches("password123", "encodedpassword");
        verify(userMapper).updateById(any(User.class));
    }

    @Test
    void testLogin_UserNotFound() {
        // 准备测试数据
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            userService.login(testUserLoginDTO);
        });

        // 验证方法调用
        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(userMapper, never()).updateById(any(User.class));
    }

    @Test
    void testLogin_WrongPassword() {
        // 准备测试数据
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);
        when(passwordEncoder.matches("password123", "encodedpassword")).thenReturn(false);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            userService.login(testUserLoginDTO);
        });

        // 验证方法调用
        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(passwordEncoder).matches("password123", "encodedpassword");
        verify(userMapper, never()).updateById(any(User.class));
    }

    @Test
    void testLogin_UserDisabled() {
        // 准备测试数据
        testUser.setStatus(0);
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            userService.login(testUserLoginDTO);
        });

        // 验证方法调用
        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(userMapper, never()).updateById(any(User.class));
    }

    @Test
    void testFindById_Success() {
        // 准备测试数据
        when(userMapper.selectById(1L)).thenReturn(testUser);

        // 执行测试
        User result = userService.findById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getNickname(), result.getNickname());

        // 验证方法调用
        verify(userMapper).selectById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // 准备测试数据
        when(userMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        User result = userService.findById(1L);

        // 验证结果
        assertNull(result);

        // 验证方法调用
        verify(userMapper).selectById(1L);
    }

    @Test
    void testFindByUsername_Success() {
        // 准备测试数据
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);

        // 执行测试
        User result = userService.findByUsername("testuser");

        // 验证结果
        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getNickname(), result.getNickname());

        // 验证方法调用
        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
    }

    @Test
    void testFindByUsername_NotFound() {
        // 准备测试数据
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // 执行测试
        User result = userService.findByUsername("nonexistent");

        // 验证结果
        assertNull(result);

        // 验证方法调用
        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
    }

    @Test
    void testFindByEmail_Success() {
        // 准备测试数据
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);

        // 执行测试
        User result = userService.findByEmail("test@example.com");

        // 验证结果
        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getUsername(), result.getUsername());

        // 验证方法调用
        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
    }

    @Test
    void testFindByEmail_NotFound() {
        // 准备测试数据
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // 执行测试
        User result = userService.findByEmail("nonexistent@example.com");

        // 验证结果
        assertNull(result);

        // 验证方法调用
        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
    }

    @Test
    void testUpdateUser_Success() {
        // 准备测试数据
        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setNickname("更新后的昵称");
        updateUser.setEmail("updated@example.com");

        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // 执行测试
        boolean result = userService.updateUser(1L, updateUser);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(userMapper).selectById(1L);
        verify(userMapper).updateById(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        // 准备测试数据
        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setNickname("更新后的昵称");

        when(userMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        boolean result = userService.updateUser(1L, updateUser);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(userMapper).selectById(1L);
        verify(userMapper, never()).updateById(any(User.class));
    }

    @Test
    void testUpdatePassword_Success() {
        // 准备测试数据
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(passwordEncoder.matches("oldpassword", "encodedpassword")).thenReturn(true);
        when(passwordEncoder.encode("newpassword")).thenReturn("newencodedpassword");
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // 执行测试
        boolean result = userService.updatePassword(1L, "oldpassword", "newpassword");

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(userMapper).selectById(1L);
        verify(passwordEncoder).matches("oldpassword", "encodedpassword");
        verify(passwordEncoder).encode("newpassword");
        verify(userMapper).updateById(any(User.class));
    }

    @Test
    void testUpdatePassword_UserNotFound() {
        // 准备测试数据
        when(userMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        boolean result = userService.updatePassword(1L, "oldpassword", "newpassword");

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(userMapper).selectById(1L);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userMapper, never()).updateById(any(User.class));
    }

    @Test
    void testUpdatePassword_WrongOldPassword() {
        // 准备测试数据
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(passwordEncoder.matches("wrongpassword", "encodedpassword")).thenReturn(false);

        // 执行测试
        boolean result = userService.updatePassword(1L, "wrongpassword", "newpassword");

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(userMapper).selectById(1L);
        verify(passwordEncoder).matches("wrongpassword", "encodedpassword");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userMapper, never()).updateById(any(User.class));
    }

    @Test
    void testGetUserPage_Success() {
        // 准备测试数据
        List<User> users = Arrays.asList(testUser);
        Page<User> page = new Page<>(1, 10);
        page.setRecords(users);
        page.setTotal(1);

        when(userMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(page);

        // 执行测试
        IPage<User> result = userService.getUserPage(1, 10, "test");

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        assertEquals(testUser.getUsername(), result.getRecords().get(0).getUsername());

        // 验证方法调用
        verify(userMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void testGetUserPage_EmptyResult() {
        // 准备测试数据
        Page<User> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList());
        page.setTotal(0);

        when(userMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(page);

        // 执行测试
        IPage<User> result = userService.getUserPage(1, 10, "nonexistent");

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertEquals(0, result.getRecords().size());

        // 验证方法调用
        verify(userMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void testDeleteUser_Success() {
        // 准备测试数据
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.deleteById(1L)).thenReturn(1);

        // 执行测试
        boolean result = userService.deleteUser(1L);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(userMapper).selectById(1L);
        verify(userMapper).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        // 准备测试数据
        when(userMapper.selectById(1L)).thenReturn(null);

        // 执行测试
        boolean result = userService.deleteUser(1L);

        // 验证结果
        assertFalse(result);

        // 验证方法调用
        verify(userMapper).selectById(1L);
        verify(userMapper, never()).deleteById(anyLong());
    }

    @Test
    void testEnableUser_Success() {
        // 准备测试数据
        testUser.setStatus(0);
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // 执行测试
        boolean result = userService.enableUser(1L);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(userMapper).selectById(1L);
        verify(userMapper).updateById(any(User.class));
    }

    @Test
    void testDisableUser_Success() {
        // 准备测试数据
        testUser.setStatus(1);
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // 执行测试
        boolean result = userService.disableUser(1L);

        // 验证结果
        assertTrue(result);

        // 验证方法调用
        verify(userMapper).selectById(1L);
        verify(userMapper).updateById(any(User.class));
    }
}
