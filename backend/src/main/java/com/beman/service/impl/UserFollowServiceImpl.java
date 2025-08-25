package com.beman.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.mapper.UserFollowMapper;
import com.beman.model.UserFollow;
import com.beman.model.User;
import com.beman.model.dto.UserFollowDTO;
import com.beman.model.dto.UserFollowQueryDTO;
import com.beman.model.vo.UserFollowVO;
import com.beman.service.UserFollowService;
import com.beman.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户关注服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserFollowServiceImpl implements UserFollowService {

    private final UserFollowMapper userFollowMapper;
    private final UserService userService;

    @Override
    @Transactional
    public UserFollow followUser(UserFollowDTO followDTO) {
        // 获取当前用户ID
        Long followerId = StpUtil.getLoginIdAsLong();
        
        // 检查是否关注自己
        if (followerId.equals(followDTO.getFollowingId())) {
            throw new RuntimeException("不能关注自己");
        }
        
        // 检查被关注用户是否存在
        User followingUser = userService.findById(followDTO.getFollowingId());
        if (followingUser == null) {
            throw new RuntimeException("被关注用户不存在");
        }
        
        // 检查是否已经关注
        UserFollow existingFollow = userFollowMapper.selectFollowRelation(followerId, followDTO.getFollowingId());
        if (existingFollow != null) {
            if (existingFollow.getStatus() == 0) {
                throw new RuntimeException("已经关注该用户");
            }
            // 如果之前取消过关注，重新关注
            existingFollow.setStatus(0);
            existingFollow.setFollowType(followDTO.getFollowType());
            existingFollow.setRemarkName(followDTO.getRemarkName());
            existingFollow.setUpdateTime(LocalDateTime.now());
            userFollowMapper.updateById(existingFollow);
            return existingFollow;
        }
        
        // 创建新的关注关系
        UserFollow userFollow = new UserFollow();
        userFollow.setFollowerId(followerId);
        userFollow.setFollowingId(followDTO.getFollowingId());
        userFollow.setStatus(0);
        userFollow.setFollowType(followDTO.getFollowType());
        userFollow.setRemarkName(followDTO.getRemarkName());
        userFollow.setCreateTime(LocalDateTime.now());
        userFollow.setUpdateTime(LocalDateTime.now());
        
        userFollowMapper.insert(userFollow);
        return userFollow;
    }

    @Override
    @Transactional
    public void unfollowUser(Long followingId) {
        Long followerId = StpUtil.getLoginIdAsLong();
        
        UserFollow userFollow = userFollowMapper.selectFollowRelation(followerId, followingId);
        if (userFollow == null || userFollow.getStatus() != 0) {
            throw new RuntimeException("未关注该用户");
        }
        
        // 逻辑删除关注关系
        userFollow.setStatus(1);
        userFollow.setUpdateTime(LocalDateTime.now());
        userFollowMapper.updateById(userFollow);
    }

    @Override
    @Transactional
    public UserFollow updateFollowSettings(Long followingId, UserFollowDTO followDTO) {
        Long followerId = StpUtil.getLoginIdAsLong();
        
        UserFollow userFollow = userFollowMapper.selectFollowRelation(followerId, followingId);
        if (userFollow == null || userFollow.getStatus() != 0) {
            throw new RuntimeException("未关注该用户");
        }
        
        userFollow.setFollowType(followDTO.getFollowType());
        userFollow.setRemarkName(followDTO.getRemarkName());
        userFollow.setUpdateTime(LocalDateTime.now());
        
        userFollowMapper.updateById(userFollow);
        return userFollow;
    }

    @Override
    public IPage<UserFollowVO> getFollowingPage(UserFollowQueryDTO queryDTO) {
        // 如果没有指定用户ID，使用当前登录用户
        if (queryDTO.getUserId() == null) {
            queryDTO.setUserId(StpUtil.getLoginIdAsLong());
        }
        
        Page<UserFollowVO> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        
        IPage<UserFollowVO> result = userFollowMapper.selectFollowingPage(
            page, queryDTO.getUserId(), queryDTO.getFollowType(), queryDTO.getKeyword()
        );
        
        // 设置互相关注状态
        result.getRecords().forEach(follow -> {
            follow.setIsMutualFollow(userFollowMapper.selectIsFollowing(follow.getUserId(), queryDTO.getUserId()));
        });
        
        return result;
    }

    @Override
    public IPage<UserFollowVO> getFollowersPage(UserFollowQueryDTO queryDTO) {
        // 如果没有指定用户ID，使用当前登录用户
        if (queryDTO.getUserId() == null) {
            queryDTO.setUserId(StpUtil.getLoginIdAsLong());
        }
        
        Page<UserFollowVO> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        
        IPage<UserFollowVO> result = userFollowMapper.selectFollowersPage(
            page, queryDTO.getUserId(), queryDTO.getFollowType(), queryDTO.getKeyword()
        );
        
        // 设置互相关注状态
        result.getRecords().forEach(follower -> {
            follower.setIsMutualFollow(userFollowMapper.selectIsFollowing(queryDTO.getUserId(), follower.getUserId()));
        });
        
        return result;
    }

    @Override
    public Boolean isFollowing(Long followingId) {
        Long followerId = StpUtil.getLoginIdAsLong();
        return userFollowMapper.selectIsFollowing(followerId, followingId);
    }

    @Override
    public Integer getFollowingCount(Long userId) {
        return userFollowMapper.selectFollowingCount(userId);
    }

    @Override
    public Integer getFollowersCount(Long userId) {
        return userFollowMapper.selectFollowersCount(userId);
    }

    @Override
    public List<UserFollowVO> getMutualFollowUsers(Long userId) {
        List<Long> mutualUserIds = userFollowMapper.selectMutualFollowUserIds(userId);
        
        return mutualUserIds.stream()
            .map(mutualUserId -> {
                UserFollowVO followVO = new UserFollowVO();
                User user = userService.findById(mutualUserId);
                if (user != null) {
                    followVO.setUserId(user.getId());
                    followVO.setUsername(user.getUsername());
                    followVO.setNickname(user.getNickname());
                    followVO.setAvatar(user.getAvatar());
                    followVO.setUserStatus(user.getStatus());
                    followVO.setLastActiveTime(user.getLastLoginTime());
                    followVO.setIsMutualFollow(true);
                }
                return followVO;
            })
            .filter(followVO -> followVO.getUserId() != null)
            .collect(Collectors.toList());
    }

    @Override
    public List<Boolean> batchCheckFollowStatus(List<Long> userIds) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        
        return userIds.stream()
            .map(userId -> userFollowMapper.selectIsFollowing(currentUserId, userId))
            .collect(Collectors.toList());
    }
}
