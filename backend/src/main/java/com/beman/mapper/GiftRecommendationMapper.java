package com.beman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.model.GiftRecommendation;
import com.beman.model.dto.GiftRecommendationQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 礼物推荐Mapper接口
 */
@Mapper
public interface GiftRecommendationMapper extends BaseMapper<GiftRecommendation> {

    /**
     * 分页查询礼物推荐
     */
    IPage<GiftRecommendation> selectGiftRecommendationPage(Page<GiftRecommendation> page, @Param("query") GiftRecommendationQueryDTO queryDTO);

    /**
     * 根据场合查询礼物推荐
     */
    List<GiftRecommendation> selectByOccasion(@Param("occasion") String occasion, @Param("limit") Integer limit);

    /**
     * 根据价格范围查询礼物推荐
     */
    List<GiftRecommendation> selectByPriceRange(
            @Param("minPrice") BigDecimal minPrice, 
            @Param("maxPrice") BigDecimal maxPrice, 
            @Param("limit") Integer limit
    );

    /**
     * 根据标签查询礼物推荐
     */
    List<GiftRecommendation> selectByTags(@Param("tags") List<String> tags, @Param("limit") Integer limit);

    /**
     * 获取热门礼物推荐（按推荐指数排序）
     */
    List<GiftRecommendation> selectPopularRecommendations(@Param("limit") Integer limit);

    /**
     * 智能推荐查询
     * 根据年龄、性别、场合、预算等条件智能匹配
     */
    List<GiftRecommendation> selectSmartRecommendations(
            @Param("targetAge") Integer targetAge,
            @Param("targetGender") Integer targetGender,
            @Param("occasions") List<String> occasions,
            @Param("maxBudget") BigDecimal maxBudget,
            @Param("limit") Integer limit
    );

    /**
     * 统计信息查询
     */
    int selectTotalCount();
    int selectEnabledCount();
    int selectDisabledCount();
    int selectHighRatingCount();
    int selectLowPriceCount();
    int selectMediumPriceCount();
    int selectHighPriceCount();
}
