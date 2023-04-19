package com.example.springboot.service.impl;

import com.example.springboot.entity.Posts;
import com.example.springboot.mapper.PostsMapper;
import com.example.springboot.service.IPostsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-04-19
 */
@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts> implements IPostsService {

}
