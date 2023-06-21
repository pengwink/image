package com.example.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-05-04
 */
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("select c.*,u.nickname,u.avatar_url from comment c left join user u on c.user_id = u.id " +
            "where c.posts_id = #{postsId} order by c.id desc")
    List<Comment> findCommentDetail(Integer postsId);

    Page<Comment> findPage(Page<Comment> page, String name);
}
