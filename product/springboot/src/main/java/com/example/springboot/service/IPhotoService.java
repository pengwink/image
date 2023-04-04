package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Photo;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
