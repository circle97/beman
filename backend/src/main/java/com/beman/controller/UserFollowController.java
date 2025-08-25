package com.beman.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beman.model.UserFollow;
import com.beman.model.dto.UserFollowDTO;
import com.beman.model.dto.UserFollowQueryDTO;
import com.beman.model.vo.Result;
import com.beman.model.vo.UserFollowVO;
import com.beman.service.UserFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户关注控制器
 */
@RestController
@RequestMapping("/api/user-follow")
@RequiredArgsConstructor
@Validated
public class UserFollowController {

    private final UserFollowService userFollowService;

    /**
     * 关注用户
     */
    @PostMapping
    public Result<UserFollow> followUser(@Valid @RequestBody UserFollowDTO followDTO) {
        try {
            UserFollow userFollow = userFollowService.followUser(followDTO);
            return Result.success("关注成功", userFollow);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消关注
     */
    @DeleteMapping("/{followingId}")
    public Result<String> unfollowUser(@PathVariable Long followingId) {
        try {
            userFollowService.unfollowUser(followingId);
            return Result.success("取消关注成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新关注设置
     */
    @PutMapping("/{followingId}")
    public Result<UserFollow> updateFollowSettings(@PathVariable Long followingId, 
                                                 @Valid @RequestBody UserFollowDTO followDTO) {
        try {
            UserFollow userFollow = userFollowService.updateFollowSettings(followingId, followDTO);
            return Result.success("设置更新成功", userFollow);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询关注列表
     */
    @GetMapping("/following")
    public Result<IPage<UserFollowVO>> getFollowingPage(UserFollowQueryDTO queryDTO) {
        try {
            IPage<UserFollowVO> result = userFollowService.getFollowingPage(queryDTO);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询粉丝列表
     */
    @GetMapping("/followers")
    public Result<IPage<UserFollowVO>> getFollowersPage(UserFollowQueryDTO queryDTO) {
        try {
            IPage<UserFollowVO> result = userFollowService.getFollowersPage(queryDTO);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询是否已关注
     */
    @GetMapping("/check/{followingId}")
    public Result<Boolean> isFollowing(@PathVariable Long followingId) {
        try {
            Boolean isFollowing = userFollowService.isFollowing(followingId);
            return Result.success(isFollowing);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取关注数量
     */
    @GetMapping("/count/following/{userId}")
    public Result<Integer> getFollowingCount(@PathVariable Long userId) {
        try {
            Integer count = userFollowService.getFollowingCount(userId);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取粉丝数量
     */
    @GetMapping("/count/followers/{userId}")
    public Result<Integer> getFollowersCount(@PathVariable Long userId) {
        try {
            Integer count = userFollowService.getFollowersCount(userId);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取互相关注用户列表
     */
    @GetMapping("/mutual/{userId}")
    public Result<List<UserFollowVO>> getMutualFollowUsers(@PathVariable Long userId) {
        try {
            List<UserFollowVO> users = userFollowService.getMutualFollowUsers(userId);
            return Result.success(users);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量查询关注状态
     */
    @PostMapping("/batch-check")
    public Result<List<Boolean>> batchCheckFollowStatus(@RequestBody List<Long> userIds) {
        try {
            List<Boolean> results = userFollowService.batchCheckFollowStatus(userIds);
            return Result.success(results);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
