package com.example.springboot.mapper;

import com.example.springboot.entity.Photo;
import com.example.springboot.entity.Type;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-31
 */
public interface TypeMapper extends BaseMapper<Type> {

    Type matche(@Param("name") String name);
    Type matcheType(@Param("name") String name);
    @Select("select * from type where id in (select type_id from photo where user_id =#{userId} )")
    List<Type> findImage(Integer userId);

    @Select("select * from photo  where user_id=#{userId} and type_id in (select id from type where id=#{id}) group by id order by id desc ")
    List<Photo> findPhoto(Integer id,Integer userId);


    List<Type> typeAllPhoto();
}
