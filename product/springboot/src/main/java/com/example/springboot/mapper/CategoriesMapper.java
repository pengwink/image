package com.example.springboot.mapper;

import com.example.springboot.entity.Categories;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-04-23
 */
public interface CategoriesMapper extends BaseMapper<Categories> {

    int add(Categories category);
}
