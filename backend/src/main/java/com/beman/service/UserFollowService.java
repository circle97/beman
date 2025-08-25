package com.beman.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beman.model.UserFollow;
import com.beman.model.dto.UserFollowDTO;
import com.beman.model.dto.UserFollowQueryDTO;
import com.beman.model.vo.UserFollowVO;

import java.util.List;

/**
 * 用户关注服务接口
 */
public interface UserFollowService {

    /**
     * 关注用户
     */
    UserFollow followUser(UserFollowDTO followDTO);

    /**
     * 取消关注
     */
    void unfollowUser(Long followingId);

    /**
     * 更新关注设置
     */
    UserFollow updateFollowSettings(Long followingId, UserFollowDTO followDTO);

    /**
     * 分页查询关注列表
     */
    IPage<UserFollowVO> getFollowingPage(UserFollowQueryDTO queryDTO);

    /**
     * 分页查询粉丝列表
     */
    IPage<UserFollowVO> getFollowersPage(UserFollowQueryDTO queryDTO);

    /**
     * 查询用户是否已关注
     */
    Boolean isFollowing(Long followingId);

    /**
     * 获取关注数量
     */
    Integer getFollowingCount(Long userId);

    /**
     * 获取粉丝数量
     */
    Integer getFollowersCount(Long userId);

    /**
     * 获取互相关注的用户列表
     */
    List<UserFollowVO> getMutualFollowUsers(Long userId);

    /**
     * 批量查询关注状态
     */
    List<Boolean> batchCheckFollowStatus(List<Long> userIds);
}
