package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Photo;
import com.example.springboot.entity.Posts;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.PostsEnca;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-04-19
 */
public interface IPostsService extends IService<Posts> {

    void addPosts(HttpServletRequest req, List<Posts> imgList, String typeName,List<Integer> imageId);

    List<Posts> selectAl();

    List<Photo> selectAlll(Integer id,Integer uid);


    PostsEnca selectPostPage(Integer userId, Integer currentPage, Integer pageSize);

    Object findpage(Page<Object> objectPage, QueryWrapper<Posts> queryWrapper);
}
