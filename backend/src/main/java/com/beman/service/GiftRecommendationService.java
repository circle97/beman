package com.beman.service;

import com.beman.model.GiftRecommendation;
import com.beman.model.dto.GiftRecommendationCreateDTO;
import com.beman.model.dto.GiftRecommendationQueryDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.math.BigDecimal;

/**
 * 礼物推荐服务接口
 */
public interface GiftRecommendationService {

    /**
     * 创建礼物推荐
     */
    GiftRecommendation createGiftRecommendation(GiftRecommendationCreateDTO createDTO);

    /**
     * 更新礼物推荐
     */
    GiftRecommendation updateGiftRecommendation(Long id, GiftRecommendationCreateDTO updateDTO);

    /**
     * 删除礼物推荐
     */
    void deleteGiftRecommendation(Long id);

    /**
     * 获取礼物推荐详情
     */
    GiftRecommendation getGiftRecommendationDetail(Long id);

    /**
     * 分页查询礼物推荐
     */
    IPage<GiftRecommendation> getGiftRecommendationPage(GiftRecommendationQueryDTO queryDTO);

    /**
     * 智能推荐礼物
     * 根据用户画像和场合自动推荐
     */
    List<GiftRecommendation> getSmartRecommendations(
            Integer targetAge, 
            Integer targetGender, 
            List<String> occasions, 
            BigDecimal maxBudget,
            Integer limit
    );

    /**
     * 根据场合推荐礼物
     */
    List<GiftRecommendation> getRecommendationsByOccasion(String occasion, Integer limit);

    /**
     * 根据价格范围推荐礼物
     */
    List<GiftRecommendation> getRecommendationsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Integer limit);

    /**
     * 根据标签推荐礼物
     */
    List<GiftRecommendation> getRecommendationsByTags(List<String> tags, Integer limit);

    /**
     * 获取热门礼物推荐
     */
    List<GiftRecommendation> getPopularRecommendations(Integer limit);

    /**
     * 启用/禁用礼物推荐
     */
    void toggleGiftRecommendationStatus(Long id, Integer status);

    /**
     * 获取礼物推荐统计信息
     */
    GiftRecommendationStats getGiftRecommendationStats();

    /**
     * 礼物推荐统计信息
     */
    interface GiftRecommendationStats {
        int getTotalCount();
        int getEnabledCount();
        int getDisabledCount();
        int getHighRatingCount(); // 4-5星推荐数量
        int getLowPriceCount(); // 100元以下推荐数量
        int getMediumPriceCount(); // 100-500元推荐数量
        int getHighPriceCount(); // 500元以上推荐数量
    }
}
