package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.AllTimeType;
import com.example.springboot.entity.ImageEnca;
import com.example.springboot.entity.Photo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-31
 */
public interface IPhotoService extends IService<Photo> {

    Page<Photo> findpage(Page<Photo> objectPage, String name);

    public Photo getUserId(Photo photo);

    public Photo getAlbumId(Photo photo);

    public Photo getTypeId(Photo photo);

    public boolean isChinese(String name);

    AllTimeType selectTimeType(Integer userId);

    ImageEnca selectAllImage(Integer userId, Integer currentPage, Integer pageSize);

    ImageEnca selectAllImageByType(Integer userId, Integer currentPage, Integer pageSize, String imageType);

    ImageEnca selectAllImageByTime(Integer userId, Integer currentPage, Integer pageSize, String imageDate);

    void uploadImage(HttpServletRequest req, List<Photo> imgList, Integer userId, Integer albumId, String albumName, String imgType);
    List<Photo> selectPhoto(Integer id);


    //    查询指定相册下的图片
    List<Photo> albumPhoto(Integer id);

    ImageEnca selectCollectImage(Integer userId, Integer currentPage, Integer pageSize);

    ImageEnca selectAgreeTop(Integer limit);

    ImageEnca selectCollectTop(Integer limit);

    List<Photo> selectPhotoName(String keywords, Integer userId);
}
