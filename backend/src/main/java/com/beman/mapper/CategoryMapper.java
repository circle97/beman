package com.beman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beman.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类Mapper
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 根据用户ID和类型查询分类列表
     */
    List<Category> selectByUserIdAndType(@Param("userId") Long userId, @Param("type") Integer type);

    /**
     * 查询系统默认分类
     */
    List<Category> selectSystemCategories(@Param("type") Integer type);
}
