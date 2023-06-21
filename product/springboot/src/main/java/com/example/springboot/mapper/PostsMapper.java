package com.example.springboot.mapper;

import cn.hutool.json.JSONUtil;
import com.example.springboot.entity.Posts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-04-19
 */
public interface PostsMapper extends BaseMapper<Posts> {
    /**
     *
     * @param imgList 发布对象集合
     * @return 自动将插入成功的图片id封装到传入参数
     */
    int addPosts(List<Posts> imgList);


    List<Posts> selectAl();
    @Select("select * from posts where is_pass = 1 and user_id =#{userId}")
    @Results(id="selectPostsPage" ,value = {
            @Result(property = "id",column = "id"),
    })
    List<Posts> selectPostsPage(Integer userId);
    @Select("select count(*) from posts where user_id=#{userId} and is_pass = 1")
    Integer selectPostsTotalCount(Integer userId);
}
