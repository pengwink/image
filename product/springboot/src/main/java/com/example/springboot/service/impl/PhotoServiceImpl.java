package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.entity.*;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.AlbumMapper;
import com.example.springboot.mapper.PhotoMapper;
import com.example.springboot.mapper.TypeMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IPhotoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-31
 */
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements IPhotoService {
    @Resource
    private TypeMapper typeMapper;
    @Resource
    private AlbumMapper albumMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private PhotoMapper photoMapper;

    private static String CHINESE_LETTER_DIGIT_REGEX = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
    @Override
    public Page<Photo> findpage(Page<Photo> page, String name) {
        return photoMapper.findPage(page, name);
    }

    /**
     * 根据用户昵称来获取用户id设置photo关联的用户id
     * @param photo
     * @return
     */
    @Override
    public Photo getUserId(Photo photo) {
        QueryWrapper<User> query = new QueryWrapper<User>();
        User user = new User();
        try {
            String name = photo.getUserNickname();
            query.eq("nickname",photo.getUserNickname());
            user = userMapper.selectOne(query);
            photo.setUserId(user.getId());
        }catch (Exception e) {
            throw new ServiceException(Constants.CODE_600, "找不到该用户");
        }
        return photo;
    }
    /**
     * 根据相册名来获取用户id设置photo关联的用户id
     * @param photo
     * @return
     */
    @Override
    public Photo getAlbumId(Photo photo) {
        QueryWrapper<Album> query = new QueryWrapper<Album>();
        Album album = new Album();
        if (photo.getAlbumName().isEmpty() & photo.getUserId()!=null){
            return photo;
        }
        try {
            query.eq("user_id",photo.getUserId());
            query.eq("name",photo.getAlbumName());
            album = albumMapper.selectOne(query);
            if (album==null){
                throw new ServiceException(Constants.CODE_700, "找不到该相册");
            }
            photo.setAlbumId(album.getId());
        }catch (ServiceException e) {
            throw new ServiceException(Constants.CODE_700, "找不到该相册");
        }
        return photo;
    }
    /**
     * 根据类型名来获取用户id设置photo关联的用户id
     * @param photo
     * @return
     */
    @Override
    public Photo getTypeId(Photo photo) {
        QueryWrapper<Type> query = new QueryWrapper<Type>();
        Type type = new Type();
        String name = photo.getTypeName();
        if (name.equals("")||name.equals(null)){
            return photo;
        }
        try {
            query.eq("name",photo.getTypeName());
            type = typeMapper.selectOne(query);
            photo.setTypeId(type.getId());
        }catch (Exception e) {
            throw new ServiceException(Constants.CODE_800, "找不到该类型");
        }
        return photo;
    }
    public boolean isChinese(String name) {
        if(name.length()>6){
            return false;
        }
        return name.matches(CHINESE_LETTER_DIGIT_REGEX);
    }
    /**
     * 查询图片时间和类型
     * @param userId
     * @return
     */
    @Override
    public AllTimeType selectTimeType(Integer userId) {
        Set<String> dates = photoMapper.selectAllImageTime(userId);
        //将null时间删掉
//        for(int i=0;i<dates.size();i++){
//            if(dates.get==null){
//                dates.remove(i);
//            }
//        }
        List<String> datel = new ArrayList<>(dates);
        Collections.sort(datel);
        List<String> strings = photoMapper.selectAllImageType(userId);
        AllTimeType allTimeTypeVO = new AllTimeType(strings,datel);
        return allTimeTypeVO;
    }
    /**
     * 分页查询指定用户所有图片
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public ImageEnca selectAllImage(Integer userId, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        List<Photo> images = photoMapper.selectAllImage(userId);
        List<String> pres = new ArrayList<>();
        for (Photo i:images){
            pres.add(i.getImg());
        }
        Integer integer = photoMapper.selectImageTotalCount(userId);
        ImageEnca imageEnca = new ImageEnca(images,pres,integer);
        return imageEnca;
    }

    /**
     * 分页按类型查询指定用户所有图片
     * @param userId
     * @param currentPage
     * @param pageSize
     * @param imageType
     * @return
     */
    @Override
    public ImageEnca selectAllImageByType(Integer userId, Integer currentPage, Integer pageSize,String imageType) {
        PageHelper.startPage(currentPage,pageSize);
        List<Photo> images = photoMapper.selectAllImageByType(userId,imageType);
        List<String> pres = new ArrayList<>();
        for (Photo i:images){
            pres.add(i.getImg());
        }
        Integer integer = photoMapper.selectImageCountByType(userId,imageType);
        ImageEnca imageEnca = new ImageEnca(images,pres,integer);
        return imageEnca;
    }


    /**
     * 分页按时间查询指定用户所有图片
     * @param userId
     * @param currentPage
     * @param pageSize
     * @param imageDate
     * @return
     */
    @Override
    public ImageEnca selectAllImageByTime(Integer userId, Integer currentPage, Integer pageSize, String imageDate) {
        PageHelper.startPage(currentPage,pageSize);
        List<Photo> images = photoMapper.selectAllImageByTime(userId,imageDate);
        List<String> pres = new ArrayList<>();
        for (Photo i:images){
            pres.add(i.getImg());
        }
        Integer integer = photoMapper.selectImageCountByTime(userId,imageDate);
        ImageEnca imageEnca = new ImageEnca(images,pres,integer);
        return imageEnca;
    }
//批量添加图片
    @Override
    public void uploadImage(HttpServletRequest req, List<Photo> imgList, Integer userId, Integer albumId, String albumName, String imgType) {
        //添加图片
        photoMapper.addImages(imgList);
    }

//    查询指定用户下的图片
    @Override
    public List<Photo> selectPhoto(Integer id) {
        List<Photo> images = photoMapper.selectAllImage(id);
        return images;
    }
    //    查询指定相册下的图片
    @Override
    public List<Photo> albumPhoto(Integer id) {
        List<Photo> images = photoMapper.albumPhoto(id);
        return images;
    }

    /**
     * 分页按类型查询指定用户所有图片
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public ImageEnca selectCollectImage(Integer userId, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        List<Photo> images = photoMapper.selectCollectImage(userId);
        List<String> pres = new ArrayList<>();
        for (Photo i:images){
            pres.add(i.getImg());
        }
        Integer integer = photoMapper.selectCollectCount(userId);
        ImageEnca imageEnca = new ImageEnca(images,pres,integer);
        return imageEnca;
    }

    @Override
    public ImageEnca selectAgreeTop(Integer limit) {
        List<Photo> images = photoMapper.selectAgreeTop(limit);
        List<String> pres = new ArrayList<>();
        for (Photo i:images){
            pres.add(i.getImg());
        }
        ImageEnca imageEnca = new ImageEnca(images,pres,limit);
        return imageEnca;
    }

    @Override
    public ImageEnca selectCollectTop(Integer limit) {
        List<Photo> images = photoMapper.selectCollectTop(limit);
        List<String> pres = new ArrayList<>();
        for (Photo i:images){
            pres.add(i.getImg());
        }
        ImageEnca imageEnca = new ImageEnca(images,pres,limit);
        return imageEnca;
    }

    @Override
    public List<Photo> selectPhotoName(String keywords, Integer userId) {
        QueryWrapper<Photo> query = new QueryWrapper<Photo>();
        query.eq("user_id",userId);
        query.like("name",keywords);
        List<Photo> images = photoMapper.selectList(query);
        return images;
    }
}
