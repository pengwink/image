package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Album;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.AlbumMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IAlbumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-30
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements IAlbumService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private AlbumMapper albumMapper;


    @Override
    public Page<Album> findPage(Page<Album> page, String name) {
        return albumMapper.findPage(page, name);
    }

    public Album getUserId(Album album) {
        QueryWrapper<User> query = new QueryWrapper<User>();
        User user = new User();
        try {
            String name = album.getUsername();
            query.eq("nickname",album.getUsername());
            user = userMapper.selectOne(query);
            album.setUserId(user.getId());
        }catch (Exception e) {
            throw new ServiceException(Constants.CODE_600, "找不到该用户");
        }
        return album;
    }

}
