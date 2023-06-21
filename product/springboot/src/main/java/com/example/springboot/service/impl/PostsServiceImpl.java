package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.*;
import com.example.springboot.mapper.*;
import com.example.springboot.service.IPostsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    @Resource
    private CategoriesMapper categoriesMapper;
    @Resource
    private AgreeMapper agreeMapper;
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private PostsMapper postsMapper;
    @Resource
    PostsCategoriesMapper postsCategoriesMapper;
    @Resource
    PostsPhotoMapper postsPhotoMapper;

    @Override
    public void addPosts(HttpServletRequest req, List<Posts> imgList,String typeName,List<Integer> imageId) {
        postsMapper.addPosts(imgList);
        if(typeName != null){
            Set<String> categoriesSet = new LinkedHashSet<>(Arrays.asList(typeName.split(",")));
            Categories categories = new Categories();
            for (Posts myList : imgList) {
                for(String category : categoriesSet){
                    // 查询数据库中是否已经存在相同的数据
                    QueryWrapper<Categories> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("category",category);
                    Categories categoryName = categoriesMapper.selectOne(queryWrapper);
                    Integer categoryId =null;
                    if (categoryName == null) {
                        // 不存在相同数据，进行插入操作
                        categories.setCategory(category);
                        categoriesMapper.add(categories);
                        categoryId = categories.getId();
                    }
                    else{
                        categoryId = categoryName.getId();
                    }
                    if(categoryId!=null){
                        postsCategoriesMapper.add(myList.getId(),categoryId);
                    }
                    for(Integer imageid : imageId){
                        postsPhotoMapper.add(myList.getId(),imageid);
                    }
                }

                System.out.println("插入的 ID: " + myList.getId());
            }
        }

        else {
            for (Posts myList : imgList) {
                for (Integer imageid : imageId) {
                    postsPhotoMapper.add(myList.getId(), imageid);
                }
                System.out.println("插入的 ID: " + myList.getId());
            }
        }
    }

    @Override
    public List<Posts> selectAl() {
        List<Posts> plist = postsMapper.selectAl();
        System.out.println(plist);
        for(Posts posts:plist){
            posts.getId();
            List<Categories> Clist = postsCategoriesMapper.selectCategory(posts.getId());
            posts.setPcategories(Clist);
        }
        return plist;
    }

    @Override
    public List<Photo> selectAlll(Integer id, Integer uid) {
            List<Photo> Plist = postsPhotoMapper.selectPhoto(id);
            for(Photo p:Plist){
                QueryWrapper<Agree> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id",uid);
                queryWrapper.eq("photo_id",p.getId());
                Agree a =agreeMapper.selectOne(queryWrapper);
                if (a != null){
                    p.setIsAgree(a.getIsAgree());
                }
                QueryWrapper<Collect> queryCollect = new QueryWrapper<>();
                queryCollect.eq("user_id",uid);
                queryCollect.eq("photo_id",p.getId());
                Collect c =collectMapper.selectOne(queryCollect);
                if (c != null){
                    p.setIsCollect(c.getIsCollect());
                }
            }
        return Plist;
    }

    @Override
    public PostsEnca selectPostPage(Integer userId, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        List<Posts> posts = postsMapper.selectPostsPage(userId);
        Integer total = postsMapper.selectPostsTotalCount(userId);
        PostsEnca postsenca = new PostsEnca(posts,total);
        return postsenca;
    }

    @Override
    public Object findpage(Page<Object> objectPage, QueryWrapper<Posts> queryWrapper) {
        return null;
    }
}
