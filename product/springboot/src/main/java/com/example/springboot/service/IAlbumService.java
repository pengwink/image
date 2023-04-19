package com.example.springboot.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Album;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.mapper.AlbumMapper;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-30
 */
public interface IAlbumService extends IService<Album> {

    List<Album> selectAllAlbum(Integer userId);
    public Page<Album> findPage(Page<Album> page, String name);

    public Album getUserId(Album album);
    JSONObject selectAlbumImage(Integer albumId) throws ParseException;

    List<Album> selectAll();

    void removeImageToAlbum(HttpServletRequest req, Integer albumId, List<Integer> imageId,Integer userId);

    void setAlbumCover(Integer albumId, Integer imageId);

    void addImageToAlbum(HttpServletRequest req, Integer albumId, List<Integer> imageId, Integer id);
}
