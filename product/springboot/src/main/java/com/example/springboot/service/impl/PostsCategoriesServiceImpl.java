package com.example.springboot.service.impl;

import com.example.springboot.entity.PostsCategories;
import com.example.springboot.mapper.PostsCategoriesMapper;
import com.example.springboot.service.IPostsCategoriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-04-23
 */
@Service
public class PostsCategoriesServiceImpl extends ServiceImpl<PostsCategoriesMapper, PostsCategories> implements IPostsCategoriesService {

}
