package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-05-04
 */
public interface ICommentService extends IService<Comment> {


    List<Comment> findCommentDetail(Integer postsId);

    Page<Comment> findpage(Page<Comment> page, String name);
}
