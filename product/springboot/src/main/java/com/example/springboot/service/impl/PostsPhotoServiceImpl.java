package com.example.springboot.service.impl;

import com.example.springboot.entity.PostsPhoto;
import com.example.springboot.mapper.PostsPhotoMapper;
import com.example.springboot.service.IPostsPhotoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-04-26
 */
@Service
public class PostsPhotoServiceImpl extends ServiceImpl<PostsPhotoMapper, PostsPhoto> implements IPostsPhotoService {

}
