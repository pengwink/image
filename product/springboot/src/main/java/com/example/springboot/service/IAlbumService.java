package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Album;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.mapper.AlbumMapper;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-30
 */
public interface IAlbumService extends IService<Album> {


    public Page<Album> findPage(Page<Album> page, String name);

    public Album getUserId(Album album);
}
