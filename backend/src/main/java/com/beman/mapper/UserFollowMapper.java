package com.beman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.model.UserFollow;
import com.beman.model.vo.UserFollowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户关注Mapper接口
 */
@Mapper
public interface UserFollowMapper extends BaseMapper<UserFollow> {

    /**
     * 分页查询关注列表
     */
    IPage<UserFollowVO> selectFollowingPage(Page<UserFollowVO> page, @Param("userId") Long userId, 
                                           @Param("followType") Integer followType, @Param("keyword") String keyword);

    /**
     * 分页查询粉丝列表
     */
    IPage<UserFollowVO> selectFollowersPage(Page<UserFollowVO> page, @Param("userId") Long userId, 
                                           @Param("followType") Integer followType, @Param("keyword") String keyword);

    /**
     * 查询用户是否已关注
     */
    Boolean selectIsFollowing(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    /**
     * 查询关注关系
     */
    UserFollow selectFollowRelation(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    /**
     * 查询关注数量
     */
    Integer selectFollowingCount(@Param("userId") Long userId);

    /**
     * 查询粉丝数量
     */
    Integer selectFollowersCount(@Param("userId") Long userId);

    /**
     * 查询互相关注的用户ID列表
     */
    List<Long> selectMutualFollowUserIds(@Param("userId") Long userId);
}
