package com.example.springboot.mapper;

import com.example.springboot.entity.Categories;
import com.example.springboot.entity.PostsCategories;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-04-23
 */
public interface PostsCategoriesMapper extends BaseMapper<PostsCategories> {

    @Insert("insert into posts_categories values (null,#{postsId},#{categoryId})")
    void add(Integer postsId, Integer categoryId);

    List<Categories> selectCategory(@Param("pid")Integer pid);
}
