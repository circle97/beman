package com.beman.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beman.mapper.CategoryMapper;
import com.beman.model.Category;
import com.beman.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 分类服务实现类
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getUserCategories(Integer type) {
        Long userId = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
        List<Category> result = new ArrayList<>();
        // 系统默认分类（user_id=0）
        QueryWrapper<Category> sysWrapper = new QueryWrapper<>();
        sysWrapper.eq("user_id", 0).eq("type", type).eq("enabled", 1).orderByAsc("sort_order");
        result.addAll(categoryMapper.selectList(sysWrapper));

        // 用户自定义分类
        if (userId != null) {
            QueryWrapper<Category> userWrapper = new QueryWrapper<>();
            userWrapper.eq("user_id", userId).eq("type", type).eq("enabled", 1).orderByAsc("sort_order");
            result.addAll(categoryMapper.selectList(userWrapper));
        }
        return result;
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        Long userId = StpUtil.getLoginIdAsLong();
        category.setUserId(userId);
        categoryMapper.insert(category);
        return category;
    }

    @Override
    @Transactional
    public Category updateCategory(Category category) {
        Long userId = StpUtil.getLoginIdAsLong();
        Category db = categoryMapper.selectById(category.getId());
        if (db == null || !Objects.equals(db.getUserId(), userId)) {
            throw new RuntimeException("分类不存在或无权限");
        }
        category.setUserId(userId);
        categoryMapper.updateById(category);
        return categoryMapper.selectById(category.getId());
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Category db = categoryMapper.selectById(id);
        if (db == null || !Objects.equals(db.getUserId(), userId)) {
            throw new RuntimeException("分类不存在或无权限");
        }
        categoryMapper.deleteById(id);
    }

    @Override
    public Category getCategoryById(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Category db = categoryMapper.selectById(id);
        if (db == null || !(db.getUserId() == 0 || Objects.equals(db.getUserId(), userId))) {
            throw new RuntimeException("分类不存在或无权限");
        }
        return db;
    }
}


