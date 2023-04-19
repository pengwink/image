package com.example.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Album;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.Photo;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-30
 */
public interface AlbumMapper extends BaseMapper<Album> {

    Page<Album> findPage(Page<Album> page,@Param("name") String name);

    @Select(" select album.id,album.name,album_img,count(DISTINCT photo.id) as imageNumber from album  left join photo on photo.album_id = album.id  where album.user_id=#{userId}  group by album.id")
    @Results(id="AlbumResultMap" ,value = {
            @Result(property = "id",column = "id"),
    })
    List<Album> selectAllAlbum(Integer userId);

    /**
     * 根据相册id查询所有图片
     * @param albumId
     * @return
     */
    @Select("select id as imageId ,img,photo_time,name from photo  where album_id in (select id from album where id=#{albumId}) ")
    List<Photo> selectAlbumImage(Integer albumId);

    /**
     * 根据相册id查询所有时间段
     * @param albumId
     * @return
     */
    @Select("select photo_time from photo  where id in (select id from album where id=#{albumId}) group by photo_time order by photo_time desc ")
    List<Date> selectAlbumImageTime(Integer albumId);
    @Select("select album.id,album.name,album_img,count(DISTINCT photo.id) as imageNumber from album  left join photo on photo.album_id = album.id  group by album.id")
    List<Album> selectAll();

    void removeAlbumImage(@Param("albumId") int albumId,@Param("list") List<Integer> imageIds);

    @Update("update album set album_img = (select img from photo where photo.id=#{imageId}) where album.id =#{albumId} ")
    void uploadAlbum(Integer albumId,Integer imageId);

    void addAlbumImage(int albumId,@Param("list") List<Integer> imageIds);
}
