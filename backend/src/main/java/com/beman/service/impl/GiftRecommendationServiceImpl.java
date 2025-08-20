package com.beman.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beman.mapper.GiftRecommendationMapper;
import com.beman.model.GiftRecommendation;
import com.beman.model.dto.GiftRecommendationCreateDTO;
import com.beman.model.dto.GiftRecommendationQueryDTO;
import com.beman.service.GiftRecommendationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 礼物推荐服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GiftRecommendationServiceImpl implements GiftRecommendationService {

    private final GiftRecommendationMapper giftRecommendationMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public GiftRecommendation createGiftRecommendation(GiftRecommendationCreateDTO createDTO) {
        GiftRecommendation gift = new GiftRecommendation();
        gift.setName(createDTO.getName());
        gift.setDescription(createDTO.getDescription());
        gift.setImageUrl(createDTO.getImageUrl());
        gift.setMinPrice(createDTO.getMinPrice());
        gift.setMaxPrice(createDTO.getMaxPrice());
        gift.setMinAge(createDTO.getMinAge());
        gift.setMaxAge(createDTO.getMaxAge());
        gift.setGender(createDTO.getGender());
        gift.setRating(createDTO.getRating());
        gift.setReason(createDTO.getReason());
        gift.setPurchaseUrl(createDTO.getPurchaseUrl());
        gift.setStatus(1); // 默认启用

        // 转换JSON字段
        try {
            if (createDTO.getOccasions() != null) {
                gift.setOccasions(objectMapper.writeValueAsString(createDTO.getOccasions()));
            }
            if (createDTO.getCategories() != null) {
                gift.setCategories(objectMapper.writeValueAsString(createDTO.getCategories()));
            }
            if (createDTO.getTags() != null) {
                gift.setTags(objectMapper.writeValueAsString(createDTO.getTags()));
            }
        } catch (Exception e) {
            log.error("转换JSON字段失败", e);
            throw new RuntimeException("数据格式错误");
        }

        giftRecommendationMapper.insert(gift);
        return gift;
    }

    @Override
    @Transactional
    public GiftRecommendation updateGiftRecommendation(Long id, GiftRecommendationCreateDTO updateDTO) {
        GiftRecommendation existingGift = getGiftRecommendationDetail(id);
        if (existingGift == null) {
            throw new RuntimeException("礼物推荐不存在");
        }

        existingGift.setName(updateDTO.getName());
        existingGift.setDescription(updateDTO.getDescription());
        existingGift.setImageUrl(updateDTO.getImageUrl());
        existingGift.setMinPrice(updateDTO.getMinPrice());
        existingGift.setMaxPrice(updateDTO.getMaxPrice());
        existingGift.setMinAge(updateDTO.getMinAge());
        existingGift.setMaxAge(updateDTO.getMaxAge());
        existingGift.setGender(updateDTO.getGender());
        existingGift.setRating(updateDTO.getRating());
        existingGift.setReason(updateDTO.getReason());
        existingGift.setPurchaseUrl(updateDTO.getPurchaseUrl());

        // 转换JSON字段
        try {
            if (updateDTO.getOccasions() != null) {
                existingGift.setOccasions(objectMapper.writeValueAsString(updateDTO.getOccasions()));
            }
            if (updateDTO.getCategories() != null) {
                existingGift.setCategories(objectMapper.writeValueAsString(updateDTO.getCategories()));
            }
            if (updateDTO.getTags() != null) {
                existingGift.setTags(objectMapper.writeValueAsString(updateDTO.getTags()));
            }
        } catch (Exception e) {
            log.error("转换JSON字段失败", e);
            throw new RuntimeException("数据格式错误");
        }

        giftRecommendationMapper.updateById(existingGift);
        return existingGift;
    }

    @Override
    @Transactional
    public void deleteGiftRecommendation(Long id) {
        giftRecommendationMapper.deleteById(id);
    }

    @Override
    public GiftRecommendation getGiftRecommendationDetail(Long id) {
        return giftRecommendationMapper.selectById(id);
    }

    @Override
    public IPage<GiftRecommendation> getGiftRecommendationPage(GiftRecommendationQueryDTO queryDTO) {
        Page<GiftRecommendation> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return giftRecommendationMapper.selectGiftRecommendationPage(page, queryDTO);
    }

    @Override
    public List<GiftRecommendation> getSmartRecommendations(
            Integer targetAge, 
            Integer targetGender, 
            List<String> occasions, 
            BigDecimal maxBudget,
            Integer limit) {
        return giftRecommendationMapper.selectSmartRecommendations(targetAge, targetGender, occasions, maxBudget, limit);
    }

    @Override
    public List<GiftRecommendation> getRecommendationsByOccasion(String occasion, Integer limit) {
        return giftRecommendationMapper.selectByOccasion(occasion, limit);
    }

    @Override
    public List<GiftRecommendation> getRecommendationsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Integer limit) {
        return giftRecommendationMapper.selectByPriceRange(minPrice, maxPrice, limit);
    }

    @Override
    public List<GiftRecommendation> getRecommendationsByTags(List<String> tags, Integer limit) {
        return giftRecommendationMapper.selectByTags(tags, limit);
    }

    @Override
    public List<GiftRecommendation> getPopularRecommendations(Integer limit) {
        return giftRecommendationMapper.selectPopularRecommendations(limit);
    }

    @Override
    @Transactional
    public void toggleGiftRecommendationStatus(Long id, Integer status) {
        GiftRecommendation gift = getGiftRecommendationDetail(id);
        if (gift == null) {
            throw new RuntimeException("礼物推荐不存在");
        }
        gift.setStatus(status);
        giftRecommendationMapper.updateById(gift);
    }

    @Override
    public GiftRecommendationStats getGiftRecommendationStats() {
        return new GiftRecommendationStats() {
            @Override
            public int getTotalCount() {
                return giftRecommendationMapper.selectTotalCount();
            }

            @Override
            public int getEnabledCount() {
                return giftRecommendationMapper.selectEnabledCount();
            }

            @Override
            public int getDisabledCount() {
                return giftRecommendationMapper.selectDisabledCount();
            }

            @Override
            public int getHighRatingCount() {
                return giftRecommendationMapper.selectHighRatingCount();
            }

            @Override
            public int getLowPriceCount() {
                return giftRecommendationMapper.selectLowPriceCount();
            }

            @Override
            public int getMediumPriceCount() {
                return giftRecommendationMapper.selectMediumPriceCount();
            }

            @Override
            public int getHighPriceCount() {
                return giftRecommendationMapper.selectHighPriceCount();
            }
        };
    }
}
