package com.example.springboot.mapper;

import com.example.springboot.entity.Photo;
import com.example.springboot.entity.PostsPhoto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-04-26
 */
public interface PostsPhotoMapper extends BaseMapper<PostsPhoto> {
    @Insert("insert into posts_photo values (null,#{postsId},#{imageId})")
    void add(Integer postsId, Integer imageId);

    List<Photo> selectPhoto(@Param("pid")Integer pid);
}
