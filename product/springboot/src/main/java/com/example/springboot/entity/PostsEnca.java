package com.example.springboot.entity;


import java.util.List;

/**
 * @program: MemoryBack
 * @description 返回图片的压缩UrL和原品质UrL
 * @author: xiaohua
 * @create: 2022-09-02 14:00
 **/
public class PostsEnca {
    private List<Posts> posts;

    public List<Posts> getPosts() {
        return posts;
    }

    @Override
    public String toString() {
        return "PostsEnca{" +
                "posts=" + posts +
                ", total=" + total +
                '}';
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public PostsEnca(List<Posts> posts, Integer total) {
        this.posts = posts;
        this.total = total;
    }

    private Integer total;

}
