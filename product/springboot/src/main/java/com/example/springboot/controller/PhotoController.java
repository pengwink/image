package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.entity.*;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.TypeMapper;
import com.example.springboot.service.ITypeService;
import com.example.springboot.service.RecordService;
import com.example.springboot.utils.FileServeUtil;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IPhotoService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-31
 */
@RestController
@RequestMapping("/photo")
public class PhotoController {
    @Resource
    private FileServeUtil fileServeUtil;
    @Resource
    private ITypeService typeService;
    @Resource
    private TypeMapper typeMapper;
    @Resource
    private IPhotoService photoService;
    @Resource
    private RecordService recordService;
    private final String now = DateUtil.now();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * 图片上传
     * @param req
     * @param albumName
     * @param albumId
     * @param content
     * @param imgType
     * @param imgDate
     * @param multipartFiles
     * @return
     * @throws Exception
     */
    @RequestMapping("/upload")
    public JSONObject upload(HttpServletRequest req, String albumName, Integer albumId,String imgName, String content,String imgTypeId,String imgType,String imgDate, @RequestParam("file")MultipartFile[] multipartFiles) throws Exception {
        Integer photoRight=1;
        Integer photoStatue=1;
        Integer isStandrd = null;
        List<String> imgNameL = Arrays.asList(imgName.split(","));
        QueryWrapper<Type> queryWrapper = new QueryWrapper<>();
        System.out.println(imgTypeId);
        queryWrapper.eq("name","imgType");
        Integer typeId = null;
        Type type = typeMapper.selectOne(queryWrapper);
        JSONObject jsonObject = new JSONObject();
        User user = getUser();
        Integer userId;
        if(user!=null) {
            userId = user.getId();
        }
        else{
            jsonObject.put("status","fail");
            return jsonObject;
        }
        Date date = new Date(); // this object contains the current date value
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(imgDate.equals("null")||imgDate.equals("")){
            imgDate=formatter.format(date);
        }
        List<Photo> imgList = new ArrayList<>();
        if(multipartFiles.length ==1&&imgNameL!=null){
            String fileName = imgNameL.get(0);
            if(isNumeric(imgTypeId)){
                typeId = Integer.parseInt(imgTypeId);
            }else if(type != null){
                if(type!=null){
                    typeId = type.getId();
                }
                else{
                    typeId = -1;
                }
            }
            else{
                if(photoService.isChinese(fileName)){
                    isStandrd=1;
                    //图片名称与类别进行匹配
                    String PhotoName =fileName;
                    Type  t = typeService.matche(PhotoName);
                    Type t2 = typeService.matcheType(PhotoName);
                    if(t!=null){
                        typeId =t.getId();
                    }
                    else if (t==null && t2!=null){
                        t = typeService.matcheType(fileName);
                        typeId =t.getId();
                    }
                }else{
                    isStandrd =0;
                }
            }
            String imageUrL = fileServeUtil.uploadServe(req,multipartFiles[0]);

//            String compressUrL = fileServeUtil.CompressImage(fileServeUtil.ServPathToAP(imageUrL), (float) m.getSize());
            imgList.add(new Photo(null,albumId,userId,typeId,fileName,imageUrL,content,imgDate,photoRight,photoStatue,isStandrd));
        }else{
            //将所有上传的图片对象存入集合
//            for (MultipartFile m:multipartFiles){
                for(int i = 0;i<multipartFiles.length;i++){
                MultipartFile m = multipartFiles[i];
                String fileName = imgNameL.get(i);
                if(photoService.isChinese(fileName)){
                    isStandrd=1;
                    //图片名称与类别进行匹配
                    String PhotoName =fileName;
                    Type  t = typeService.matche(PhotoName);
                    Type t2 = typeService.matcheType(PhotoName);
                    if(t!=null){
                        typeId =t.getId();
                    }
                    else if (t==null && t2!=null){
                        t = typeService.matcheType(PhotoName);
                        typeId =t.getId();
                    }
                }else{
                    if(isNumeric(imgTypeId)){
                        typeId = Integer.parseInt(imgTypeId);
                    }else{
                        typeId=-1;
                    }
                    isStandrd =0;
                }
                String imageUrL = fileServeUtil.uploadServe(req,m);
                if(imageUrL=="fail"){
                    jsonObject.put("status","fail");
                    return jsonObject;
                }
//            String compressUrL = fileServeUtil.CompressImage(fileServeUtil.ServPathToAP(imageUrL), (float) m.getSize());
                    System.out.println(imageUrL);

                imgList.add(new Photo(null,albumId,userId,typeId,fileName,imageUrL,content,imgDate,photoRight,photoStatue,isStandrd));
            }
        }
        photoService.uploadImage(req,imgList, userId, albumId, albumName, imgType);
        if(true){
            jsonObject.put("status","success");
            recordService.addRecord(req, Operation.uploadImage.getName(),imgList.size(),userId);
        }
        else{
            for(Photo i:imgList){
                String imageUrL = i.getImg();
                fileServeUtil.deleteServe(imageUrL);
            }
            jsonObject.put("status","fail");
        }
        return jsonObject;
    }
//    // 新增或者更新
    @PostMapping
    public Result save(HttpServletRequest req, @RequestBody Photo photo) {
        if (photo.getId() == null) {
            //photo.setTime(DateUtil.now());
            //photo.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        System.out.println(photo);
        try{
            if(photo.getUserId()==null){
                photoService.getUserId(photo);
            }
            if (photo.getAlbumName()!=null&&photo.getAlbumName()!=""){
                photoService.getAlbumId(photo);
            }
            if (photo.getTypeName()!=null && photo.getTypeName()!=""){
                photoService.getTypeId(photo);
            }
            photoService.isChinese(photo.getName());
            if(photoService.isChinese(photo.getName())){
                photo.setIsStandard(1);
                //图片名称与类别进行匹配
                String PhotoName =photo.getName();
                Type  t = typeService.matche(PhotoName);
                Type t2 = typeService.matcheType(PhotoName);
                if(t!=null){
                    photo.setTypeId(t.getId());
                }
                else if (t==null && t2!=null){
                    t = typeService.matcheType(photo.getName());
                    photo.setTypeId(t.getId());
                }
            }else{
                photo.setIsStandard(0);
            }

            photoService.saveOrUpdate(photo);
        }
        catch(ServiceException e){
            System.out.println(e);
            return Result.error(e.getCode(), e.getMessage());
        }
        recordService.addRecord(req, Operation.uploadImage.getName(), 1,getUser().getId());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(HttpServletRequest req,@PathVariable Integer id) {
        photoService.removeById(id);
        recordService.addRecord(req, Operation.deleteImage.getName(), 1,getUser().getId());
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(HttpServletRequest req,@RequestBody List<Integer> ids) {
        photoService.removeByIds(ids);
        recordService.addRecord(req, Operation.deleteImage.getName(), 1,getUser().getId());
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(photoService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(photoService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
//        QueryWrapper<Photo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        if (!"".equals(name)) {
//            queryWrapper.like("name", name);
//        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        return Result.success(photoService.findpage(new Page<>(pageNum, pageSize), name));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response,HttpServletRequest req) throws Exception {
        // 从数据库查询出所有的数据
        List<Photo> list = photoService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Photo信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
        recordService.addRecord(req, Operation.exportImage.getName(), 1,getUser().getId());

        }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(HttpServletRequest req, MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Photo> list = reader.readAll(Photo.class);

        photoService.saveBatch(list);
        recordService.addRecord(req, Operation.importImage.getName(), 1,getUser().getId());
        return Result.success();
    }
    /**
     * 查询指定相册图片
     * @return
     */
    @RequestMapping("/albumPhoto")
    public JSONObject selectAlbumUser(@RequestBody String pid){
        JSONObject jsonObject = new JSONObject();
        Integer id = Integer.parseInt(pid);
        List<Photo> photoVOS =photoService.albumPhoto(id);
        jsonObject.put("status","success");
        jsonObject.put("data", photoVOS);
        return jsonObject;
    }
/**
 * 查询图片的时间和类型
 */
    @RequestMapping("/selectTimeType")
    public JSONObject selectTimeType( @RequestParam("uid") Integer uid){
        JSONObject jsonObject = new JSONObject();
        if(uid==null){
            jsonObject.put("status","fail");
            return jsonObject;
        }
        AllTimeType allTimeTypeVO = photoService.selectTimeType(uid);
        jsonObject.put("data",allTimeTypeVO);
        jsonObject.put("status","success");
        return jsonObject;
    }

    /**
     *分页查询所有
     * @return
     */
    @RequestMapping("/selectAll")
    public JSONObject selectAllImage(String token,Integer currentPage,Integer pageSize,Integer userId){
        JSONObject jsonObject = new JSONObject();
        if(userId!=null) {
            ImageEnca image = photoService.selectAllImage(userId,currentPage,pageSize);
            jsonObject.put("data",image);
            jsonObject.put("status","success");
        }
        else{
            jsonObject.put("status","fail");
        }
        return jsonObject;
    }
    /**
     *分页根据收藏查询所有图片
     * @return
     */
    @RequestMapping("/selectCollectImage")
    public JSONObject selectCollectImage(String token,Integer currentPage,Integer pageSize,Integer userId){
        JSONObject jsonObject = new JSONObject();
        if(userId!=null) {
            ImageEnca image = photoService.selectCollectImage(userId,currentPage,pageSize);
            jsonObject.put("data",image);
            jsonObject.put("status","success");
        }
        else{
            jsonObject.put("status","fail");
        }
        return jsonObject;
    }
    /**
     *查询所有
     * @return
     */
    @GetMapping("/selectPhoto")
    public Result selectPhoto(@RequestParam Integer id) {
        return Result.success(photoService.selectPhoto(id));
    }

    /**
     * 分页根据类型查询图片
     * @param token
     * @param currentPage
     * @param pageSize
     * @param imageType
     * @return
     */
    @RequestMapping("/selectAllByType")
    public JSONObject selectAllImageByType(String token,Integer currentPage,Integer pageSize,String imageType,Integer userId){
        JSONObject jsonObject = new JSONObject();
        if (userId==null){
            jsonObject.put("status","fail");
            return jsonObject;
        }
        System.out.println(imageType);
        ImageEnca  imageEnca = photoService.selectAllImageByType(userId,currentPage,pageSize,imageType);
        jsonObject.put("data",imageEnca);
        jsonObject.put("status","success");
        return jsonObject;
    }
    /**
     * 分页根据时间查询图片
     * @param token
     * @param currentPage
     * @param pageSize
     * @param imageDate
     * @return
     */
    @RequestMapping("/selectAllByTime")
    public JSONObject selectAllImageByTime(String token,Integer currentPage,Integer pageSize,String imageDate,Integer userId) throws ParseException {
        JSONObject jsonObject = new JSONObject();
//        Date imgDate = dateFormat.parse(imageDate);
        if(userId==null){
            jsonObject.put("status","fail");
            return jsonObject;
        }
        ImageEnca  imageVO = photoService.selectAllImageByTime(userId,currentPage,pageSize,imageDate);
        jsonObject.put("data",imageVO);
        jsonObject.put("status","success");
        return jsonObject;
    }
    /**
     *查询排行榜
     * @return
     */
    @RequestMapping("/selectTop")
    public JSONObject selectTop(Integer agreeCollect,Integer limit){
        JSONObject jsonObject = new JSONObject();
        ImageEnca image = new ImageEnca();
        if (agreeCollect==1){
            image = photoService.selectAgreeTop(limit);
        }else if(agreeCollect==0){
            image = photoService.selectCollectTop(limit);
        }
        if(image!=null){
            jsonObject.put("data",image);
            jsonObject.put("status","success");
        }
        else{
            jsonObject.put("status","fail");
        }
        return jsonObject;
    }

    /**
     * 根据图片名称查询
     * @return
     */
    @RequestMapping("/selectPhotoName")
    public JSONObject selectPhoto(@RequestBody String keywords){
        JSONObject jsonObject = new JSONObject();
        User user = getUser();
        Integer userId;
        if(user!=null) {
            userId = user.getId();
        }
        else{
            jsonObject.put("status","fail");
            return jsonObject;
        }

        List<Photo> images = photoService.selectPhotoName(keywords,userId);
        jsonObject.put("data",images);
        return jsonObject;
    }
    private User getUser() {
        return TokenUtils.getCurrentUser();
    }
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}

