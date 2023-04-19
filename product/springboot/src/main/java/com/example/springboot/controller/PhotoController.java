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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.entity.*;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.ITypeService;
import com.example.springboot.service.RecordService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
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
    private ITypeService typeService;
    @Resource
    private IPhotoService photoService;
    @Resource
    private RecordService recordService;
    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(HttpServletRequest req, @RequestBody Photo photo) {
        if (photo.getId() == null) {
            //photo.setTime(DateUtil.now());
            //photo.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        System.out.println(photo);
        try{
            photoService.getUserId(photo);
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
 * 查询图片的时间和类型
 */
    @RequestMapping("/selectTimeType")
    public JSONObject selectTimeType(){
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
        AllTimeType allTimeTypeVO = photoService.selectTimeType(userId);
        jsonObject.put("data",allTimeTypeVO);
        jsonObject.put("status","success");
        return jsonObject;
    }

    /**
     *分页查询所有
     * @return
     */
    @RequestMapping("/selectAll")
    public JSONObject selectAllImage(String token,Integer currentPage,Integer pageSize){
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

        ImageEnca image = photoService.selectAllImage(userId,currentPage,pageSize);
        jsonObject.put("data",image);
        jsonObject.put("status","success");
        return jsonObject;
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
    public JSONObject selectAllImageByType(String token,Integer currentPage,Integer pageSize,String imageType){
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
    public JSONObject selectAllImageByTime(String token,Integer currentPage,Integer pageSize,String imageDate) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        User user = getUser();
//        Date imgDate = dateFormat.parse(imageDate);
        System.out.println(imageDate);
        Integer userId;
        if(user!=null) {
            userId = user.getId();
        }
        else{
            jsonObject.put("status","fail");
            return jsonObject;
        }
        ImageEnca  imageVO = photoService.selectAllImageByTime(userId,currentPage,pageSize,imageDate);
        jsonObject.put("data",imageVO);
        jsonObject.put("status","success");
        return jsonObject;
    }


    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

