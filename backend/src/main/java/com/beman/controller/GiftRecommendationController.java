package com.beman.controller;

import com.beman.model.GiftRecommendation;
import com.beman.model.dto.GiftRecommendationCreateDTO;
import com.beman.model.dto.GiftRecommendationQueryDTO;
import com.beman.service.GiftRecommendationService;
import com.beman.model.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * 礼物推荐管理控制器
 */
@RestController
@RequestMapping("/api/gift-recommendation")
@RequiredArgsConstructor
public class GiftRecommendationController {

    private final GiftRecommendationService giftRecommendationService;

    /**
     * 创建礼物推荐
     */
    @PostMapping
    public Result<GiftRecommendation> createGiftRecommendation(@Valid @RequestBody GiftRecommendationCreateDTO createDTO) {
        try {
            GiftRecommendation gift = giftRecommendationService.createGiftRecommendation(createDTO);
            return Result.success(gift);
        } catch (Exception e) {
            return Result.error("创建礼物推荐失败: " + e.getMessage());
        }
    }

    /**
     * 更新礼物推荐
     */
    @PutMapping("/{id}")
    public Result<GiftRecommendation> updateGiftRecommendation(
            @PathVariable Long id, 
            @Valid @RequestBody GiftRecommendationCreateDTO updateDTO) {
        try {
            GiftRecommendation gift = giftRecommendationService.updateGiftRecommendation(id, updateDTO);
            return Result.success(gift);
        } catch (Exception e) {
            return Result.error("更新礼物推荐失败: " + e.getMessage());
        }
    }

    /**
     * 删除礼物推荐
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteGiftRecommendation(@PathVariable Long id) {
        try {
            giftRecommendationService.deleteGiftRecommendation(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除礼物推荐失败: " + e.getMessage());
        }
    }

    /**
     * 获取礼物推荐详情
     */
    @GetMapping("/{id}")
    public Result<GiftRecommendation> getGiftRecommendationDetail(@PathVariable Long id) {
        try {
            GiftRecommendation gift = giftRecommendationService.getGiftRecommendationDetail(id);
            if (gift == null) {
                return Result.error("礼物推荐不存在");
            }
            return Result.success(gift);
        } catch (Exception e) {
            return Result.error("获取礼物推荐详情失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询礼物推荐
     */
    @GetMapping("/page")
    public Result<Object> getGiftRecommendationPage(GiftRecommendationQueryDTO queryDTO) {
        try {
            Object pageResult = giftRecommendationService.getGiftRecommendationPage(queryDTO);
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("查询礼物推荐失败: " + e.getMessage());
        }
    }

    /**
     * 智能推荐礼物
     */
    @GetMapping("/smart")
    public Result<List<GiftRecommendation>> getSmartRecommendations(
            @RequestParam(required = false) Integer targetAge,
            @RequestParam(required = false) Integer targetGender,
            @RequestParam(required = false) List<String> occasions,
            @RequestParam(required = false) BigDecimal maxBudget,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<GiftRecommendation> recommendations = giftRecommendationService.getSmartRecommendations(
                    targetAge, targetGender, occasions, maxBudget, limit);
            return Result.success(recommendations);
        } catch (Exception e) {
            return Result.error("智能推荐失败: " + e.getMessage());
        }
    }

    /**
     * 根据场合推荐礼物
     */
    @GetMapping("/by-occasion")
    public Result<List<GiftRecommendation>> getRecommendationsByOccasion(
            @RequestParam String occasion,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<GiftRecommendation> recommendations = giftRecommendationService.getRecommendationsByOccasion(occasion, limit);
            return Result.success(recommendations);
        } catch (Exception e) {
            return Result.error("根据场合推荐失败: " + e.getMessage());
        }
    }

    /**
     * 根据价格范围推荐礼物
     */
    @GetMapping("/by-price")
    public Result<List<GiftRecommendation>> getRecommendationsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<GiftRecommendation> recommendations = giftRecommendationService.getRecommendationsByPriceRange(minPrice, maxPrice, limit);
            return Result.success(recommendations);
        } catch (Exception e) {
            return Result.error("根据价格范围推荐失败: " + e.getMessage());
        }
    }

    /**
     * 根据标签推荐礼物
     */
    @GetMapping("/by-tags")
    public Result<List<GiftRecommendation>> getRecommendationsByTags(
            @RequestParam List<String> tags,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<GiftRecommendation> recommendations = giftRecommendationService.getRecommendationsByTags(tags, limit);
            return Result.success(recommendations);
        } catch (Exception e) {
            return Result.error("根据标签推荐失败: " + e.getMessage());
        }
    }

    /**
     * 获取热门礼物推荐
     */
    @GetMapping("/popular")
    public Result<List<GiftRecommendation>> getPopularRecommendations(
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<GiftRecommendation> recommendations = giftRecommendationService.getPopularRecommendations(limit);
            return Result.success(recommendations);
        } catch (Exception e) {
            return Result.error("获取热门推荐失败: " + e.getMessage());
        }
    }

    /**
     * 启用/禁用礼物推荐
     */
    @PostMapping("/{id}/toggle-status")
    public Result<Void> toggleGiftRecommendationStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        try {
            giftRecommendationService.toggleGiftRecommendationStatus(id, status);
            return Result.success();
        } catch (Exception e) {
            return Result.error("切换状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取礼物推荐统计信息
     */
    @GetMapping("/stats")
    public Result<Object> getGiftRecommendationStats() {
        try {
            Object stats = giftRecommendationService.getGiftRecommendationStats();
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取统计信息失败: " + e.getMessage());
        }
    }
}
