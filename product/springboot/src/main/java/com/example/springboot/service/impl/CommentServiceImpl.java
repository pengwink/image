package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Comment;
import com.example.springboot.mapper.CommentMapper;
import com.example.springboot.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-05-04
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private CommentMapper commentMapper;
    @Override
    public List<Comment> findCommentDetail(Integer postsId) {
        return commentMapper.findCommentDetail(postsId);
    }

    @Override
    public Page<Comment> findpage(Page<Comment> page, String name) {
        return commentMapper.findPage(page,name);
    }
}
