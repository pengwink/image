package com.example.springboot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Album;
import com.example.springboot.entity.Operation;
import com.example.springboot.entity.Photo;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.AlbumMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IAlbumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.service.RecordService;
import com.example.springboot.utils.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolver.iterator;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-30
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements IAlbumService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private AlbumMapper albumMapper;
    @Resource
    DateUtil dateUtil;
    @Resource
    RecordService recordService;
    public List<Album> selectAllAlbum(Integer userId) {
        if(userId==null){
            return null;
        }
        List<Album> partAlbumVO = albumMapper.selectAllAlbum(userId);
        return partAlbumVO;
    }

    @Override
    public Page<Album> findPage(Page<Album> page, String name) {
        return albumMapper.findPage(page, name);
    }

    public Album getUserId(Album album) {
        QueryWrapper<User> query = new QueryWrapper<User>();
        User user = new User();
        try {
            String name = album.getUsername();
            query.eq("nickname",album.getUsername());
            user = userMapper.selectOne(query);
            album.setUserId(user.getId());
        }catch (Exception e) {
            throw new ServiceException(Constants.CODE_600, "找不到该用户");
        }
        return album;
    }

    /**
     * 查询相册所有图片
     * @param albumId
     * @return
     * @throws ParseException
     */
    @Override
    public JSONObject selectAlbumImage(Integer albumId) throws ParseException {
        List<Photo> images = albumMapper.selectAlbumImage(albumId);
        List<Date> dates = albumMapper.selectAlbumImageTime(albumId);
        System.out.println("遍历");
//        Iterator<Photo> it = images.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next().getImg());
//        }
        List<Album> albumImageVOS = new ArrayList<>();
        List<String> previewList = new ArrayList<>();
        System.out.println(dates);
        for(int i=0;i<dates.size();i++){
            List<Photo> img = new ArrayList<>();
            for(int j=0;j<images.size();j++){
//                懒惰表达式
                if(dates.get(i)!=null||dates.get(i).equals(images.get(j).getPhotoTime())){
                    img.add(images.get(j));
                    previewList.add(images.get(j).getImg());
                    images.remove(j);
                    j--;
                }
            }
            String time = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            if(dates.get(i)!=null) {
                String format = dateFormat.format(dates.get(i));
                String week = dateUtil.getWeek(dates.get(i));
                time = format+week;
            }
            else{
                time="其它时间";
            }
            albumImageVOS.add(new Album(time,img));
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("images",albumImageVOS);
        jsonObject.put("previewImageUrL",previewList);
        return jsonObject;
    }

    @Override
    public List<Album> selectAll() {
        List<Album> partAlbumVO = albumMapper.selectAll();
        return partAlbumVO;
    }

    @Override
    public void removeImageToAlbum(HttpServletRequest req, Integer albumId, List<Integer> imageId,Integer userId) {
        System.out.println(albumId);
        System.out.println(imageId);
        albumMapper.removeAlbumImage(albumId,imageId);
        Album album = albumMapper.selectById(albumId);

        recordService.addRecord(req, "\""+album.getName()+"\""+ Operation.deleteAlbumImage.getName(), imageId.size(),userId);

    }

    /**
     * 将图片添加至相册
     * @param albumId
     * @param imageId
     */
    @Override
    public void addImageToAlbum(HttpServletRequest req,Integer albumId, List<Integer> imageId,Integer userId) {
        albumMapper.addAlbumImage(albumId,imageId);
        Album album= albumMapper.selectById(albumId);
        String albumName = album.getName();
        recordService.addRecord(req, Operation.addImageToAlbum.getName()+"\""+albumName+"\"", imageId.size(),userId);
    }

    @Override
    public void setAlbumCover( Integer albumId, Integer imageId) {
        albumMapper.uploadAlbum(albumId,imageId);
    }




}
