package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.entity.Album;
import com.example.springboot.entity.Photo;
import com.example.springboot.entity.Type;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.AlbumMapper;
import com.example.springboot.mapper.PhotoMapper;
import com.example.springboot.mapper.TypeMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IPhotoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-31
 */
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements IPhotoService {
    @Resource
    private TypeMapper typeMapper;
    @Resource
    private AlbumMapper albumMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private PhotoMapper photoMapper;
    @Override
    public Page<Photo> findpage(Page<Photo> page, String name) {
        return photoMapper.findPage(page, name);
    }

    @Override
    public Photo getUserId(Photo photo) {
        QueryWrapper<User> query = new QueryWrapper<User>();
        User user = new User();
        try {
            String name = photo.getUserNickname();
            query.eq("nickname",photo.getUserNickname());
            user = userMapper.selectOne(query);
            photo.setUserId(user.getId());
        }catch (Exception e) {
            throw new ServiceException(Constants.CODE_600, "找不到该用户");
        }
        return photo;
    }

    @Override
    public Photo getAlbumId(Photo photo) {
        QueryWrapper<Album> query = new QueryWrapper<Album>();
        Album album = new Album();
        if (photo.getAlbumName().isEmpty()){
            return photo;
        }
        try {
            query.eq("name",photo.getAlbumName());
            album = albumMapper.selectOne(query);
            photo.setAlbumId(album.getId());
        }catch (Exception e) {
            throw new ServiceException(Constants.CODE_700, "找不到该相册");
        }
        return photo;
    }

    @Override
    public Photo getTypeId(Photo photo) {
        QueryWrapper<Type> query = new QueryWrapper<Type>();
        Type type = new Type();
        String name = photo.getTypeName();
        if (name.equals("")||name.equals(null)){
            return photo;
        }
        try {
            query.eq("name",photo.getTypeName());
            type = typeMapper.selectOne(query);
            photo.setTypeId(type.getId());
        }catch (Exception e) {
            throw new ServiceException(Constants.CODE_800, "找不到该类型");
        }
        return photo;
    }
}
