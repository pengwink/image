package com.example.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Photo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
//    @Select("select photo_time from photo where user_id =#{userId} group by photo_time")
//    List<String> selectAllImageTime(Integer userId);
    @Select("SELECT DATE_FORMAT(photo_time,'%Y-%m-%d') FROM photo where user_id =#{userId} group by photo_time")
    Set<String> selectAllImageTime(Integer userId);


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
    @Select("select count(*) from photo where user_id=#{userId} ")
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
    @Select("select * from photo where user_id = #{userId} and photo_time like  concat('%',#{imageDate},'%')" )
    @ResultMap(value = "ImageResultMap")
    List<Photo> selectAllImageByTime(Integer userId, String imageDate);
//    @ResultMap(value = "ImageResultMap")
//    List<Photo> selectAllImageByTime(Integer userId, String imageDate);
    /**
     * 8查询指定瀛湖指定时间的图片数量
     * @param userId
     * @param imageDate
     * @return
     */
    @Select("select count(*) from photo where user_id = #{userId} and photo_time = #{imageDate}")
    Integer selectImageCountByTime(Integer userId, String imageDate);
    /**
     * 根据相册id查询所有图片
     * @param albumId
     * @return
     */
    @Select("select id as imageId,album_id,user_id,type_id,content,img,photo_time,name,photo_right,photo_statue,is_standard from photo  where album_id in (select id from album where id=#{albumId}) ")
    List<Photo> albumPhoto(Integer albumId);
    /**
     *
     * @param imageList 图片对象集合
     * @return 自动将插入成功的图片id封装到传入参数
     */
    void addImages(@Param("list")List<Photo> imageList);

    @Select("select photo.*,collect.id as collectId from photo left join collect on photo.id = collect.photo_id where  photo.user_id = #{userId} and photo.id in (select photo_id from collect where is_collect = 1)")
    List<Photo> selectCollectImage(Integer userId);
    @Select("select count(*) from photo where user_id = #{userId} and id in (select photo_id from collect where is_collect = 1)")
    Integer selectCollectCount(Integer userId);

    @Select("select photo.*,COUNT(agree.photo_id) AS `agree_count`,`user`.nickname AS userNickname from photo " +
            "left join agree on photo.id = agree.photo_id " +
            "left join `user` on photo.user_id = `user`.id "+
            "WHERE photo.id in (select photo_id from agree where is_agree = 1) " +
            "GROUP BY photo.id ORDER BY `agree_count` DESC " +
            "LIMIT  #{limit}")
    List<Photo> selectAgreeTop(Integer limit);
    @Select("select photo.*,COUNT(collect.photo_id) AS `collect_count`,`user`.nickname AS userNickname from photo " +
            "left join collect on photo.id = collect.photo_id " +
            "left join `user` on photo.user_id = `user`.id "+
            "WHERE photo.id in (select photo_id from collect where is_collect = 1) " +
            "GROUP BY photo.id ORDER BY `collect_count` DESC " +
            "LIMIT  #{limit}")
    List<Photo> selectCollectTop(Integer limit);
}
