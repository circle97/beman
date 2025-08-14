package com.beman.service;

import com.beman.model.Category;

import java.util.List;

/**
 * 分类Service
 */
public interface CategoryService {

    /**
     * 获取用户分类列表
     */
    List<Category> getUserCategories(Integer type);

    /**
     * 创建分类
     */
    Category createCategory(Category category);

    /**
     * 更新分类
     */
    Category updateCategory(Category category);

    /**
     * 删除分类
     */
    void deleteCategory(Long id);

    /**
     * 获取分类详情
     */
    Category getCategoryById(Long id);
}
