package com.example.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Photo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-31
 */
public interface PhotoMapper extends BaseMapper<Photo> {

    Page<Photo> findPage(Page<Photo> page, String name);
    /**
     * 1.查询指定用户所有的图片类型
     * @param userId
     * @return
     */
    @Select("select name from type where id in (select type_id from photo where user_id=#{userId}) group by name")
    List<String> selectAllImageType(Integer userId);
    /**
     * 2.查询指定用户所有的图片时间
     * @param userId
     * @return
     */
    @Select("select photo_time from photo where user_id =#{userId} group by photo_time")
    List<String> selectAllImageTime(Integer userId);

    /**
     * 3.分页查询所有图片
     * @param userId
     * @return
     */
    @Select("select * from photo where user_id =#{userId}")
    @Results(id="ImageResultMap" ,value = {
            @Result(property = "id",column = "id"),
    })
    List<Photo> selectAllImage(Integer userId);

    /**
     * 4查询指定用户图片所有数量
     * @param userId
     * @return
     */
    @Select("select count(*) from photo where user_id=#{userId}")
    Integer selectImageTotalCount(Integer userId);

    /**
     * 5.分页查询所有符合类型的图片
     * @param userId
     * @param imageType
     * @return
     */
    @Select("select * from photo where user_id = #{userId} and type_id in (select id from type where name = #{imageType})" )
    @ResultMap(value = "ImageResultMap")
    List<Photo> selectAllImageByType(Integer userId,String imageType);

    /**
     * 6.查询指定瀛湖指定类型的图片数量
     * @param userId
     * @param imageType
     * @return
     */
    @Select("select count(*) from photo where  user_id = #{userId} and type_id in (select id from type where name = #{imageType})")
    Integer selectImageCountByType(Integer userId,String imageType);

    /**
     * 7.分页查询所有符合时间的图片
     * @param userId
     * @param imageDate
     * @return
     */
    @Select("select * from photo where user_id = #{userId} and photo_time = #{imageDate}" )
    @ResultMap(value = "ImageResultMap")
    List<Photo> selectAllImageByTime(Integer userId, String imageDate);
    /**
     * 8查询指定瀛湖指定时间的图片数量
     * @param userId
     * @param imageDate
     * @return
     */
    @Select("select count(*) from photo where user_id = #{userId} and photo_time = #{imageDate}")
    Integer selectImageCountByTime(Integer userId, String imageDate);
}
