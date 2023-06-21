package com.example.springboot.controller;
import java.text.ParseException;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.entity.Operation;
import com.example.springboot.entity.Photo;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.IUserService;
import com.example.springboot.service.RecordService;
import com.sun.xml.internal.ws.api.model.ExceptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IAlbumService;
import com.example.springboot.entity.Album;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-30
 */
@RestController
@RequestMapping("/album")
public class AlbumController {
    @Value("${files.upload.path}")
    private String filesUploadPath;
    @Resource
    private RecordService recordService;
    @Resource
    private IUserService userService;
    @Resource
    private IAlbumService albumService;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(HttpServletRequest req,@RequestBody Album album) {
        if (album.getId() == null) {
            //album.setTime(DateUtil.now());
            //album.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        try{
            if(album.getUserId()==null){
                albumService.getUserId(album);
            }
            albumService.saveOrUpdate(album);
            recordService.addRecord(req, Operation.createAlbum.getName()+"\""+album.getName()+"\"", 1,getUser().getId());
        }catch(Exception e){
            return Result.error(Constants.CODE_600,"无法找到用户信息");

        }
        return Result.success();
    }
    //设置封面
    @RequestMapping("/setAlbumCover")
    public JSONObject setAlbumCover(String token,Integer albumId,Integer imageId) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        System.out.println(getUser());
        if(getUser()==null){
            jsonObject.put("status","fail");
            return jsonObject;
        }
        albumService.setAlbumCover(albumId,imageId);
        jsonObject.put("status","success");
        return jsonObject;
    }

    /**
     * 查询用户下的相册
     * @param token
     * @return
     */
    @RequestMapping("/selectAlbumName")
    public JSONObject selectAlbumName(String token,Integer userId){
        JSONObject jsonObject = new JSONObject();
        if(userId==null){
            userId = getUser().getId();
        }
        List<Album> partAlbumVOS = albumService.selectAllAlbum(userId);
        jsonObject.put("status","success");
        jsonObject.put("data", partAlbumVOS);
        return jsonObject;
    }

    /**
     * 查询用户下的相册
     * @return
     */
    @RequestMapping("/selectAlbumUser")
    public JSONObject selectAlbumUser(@RequestBody String uid){
        JSONObject jsonObject = new JSONObject();
        List<Album> partAlbumVOS = albumService.selectAllAlbum(Integer.parseInt(uid));
        jsonObject.put("status","success");
        jsonObject.put("data", partAlbumVOS);
        return jsonObject;
    }
    @RequestMapping("/selectAllAlbumName")
    public JSONObject selectAllAlbumName(){
        JSONObject jsonObject = new JSONObject();
//        System.out.println(user.toString());
        List<Album> partAlbumVOS = albumService.selectAll();
        jsonObject.put("status","success");
        jsonObject.put("data", partAlbumVOS);
        return jsonObject;
    }
    @RequestMapping("/selectAllImage")
    public JSONObject selectAllImage(HttpServletRequest req, String token, Integer albumId) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        JSONObject res = albumService.selectAlbumImage(albumId);
        jsonObject.put("status","success");
        jsonObject.put("data",res);
        return jsonObject;
    }

    /**
     * 移除相册图片
     * @param req
     * @param imageId
     * @param albumId
     * @return
     */
    @PostMapping("/removeImageFromAlbum")
    public Result removeImageFromAlbum(HttpServletRequest req,@RequestParam("imageId") List<Integer> imageId, Integer albumId) {
        System.out.println(imageId);
        System.out.println(albumId);
        System.out.println(getUser().getId());
        albumService.removeImageToAlbum(req,albumId,imageId,getUser().getId());
        return Result.success();
    }

    /**
     * 添加相册图片
     * @param req
     * @param token
     * @param imageId
     * @param albumId
     * @return
     */
    @RequestMapping("/addImageToAlbum")
    public JSONObject addImageToAlbum(HttpServletRequest req, String token, @RequestParam("imageId") List<Integer> imageId, Integer albumId ){
        JSONObject jsonObject = new JSONObject();
        User user = getUser();
        if(user==null){
            jsonObject.put("status","fail");
            return jsonObject;
        }
        albumService.addImageToAlbum(req,albumId,imageId,user.getId());
        jsonObject.put("status","success");
        return jsonObject;
    }

    @DeleteMapping("/{id}")
    public Result delete(HttpServletRequest req,@PathVariable Integer id) {
        albumService.removeById(id);
        recordService.addRecord(req, Operation.deleteAlbum.getName(), 1,getUser().getId());
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(HttpServletRequest req,@RequestBody List<Integer> ids) {
        albumService.removeByIds(ids);
        recordService.addRecord(req, Operation.deleteBatchAlbum.getName(), 1,getUser().getId());
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(albumService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(albumService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
//        QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        if (!"".equals(name)) {
//            queryWrapper.like("name", name);
//        }
//        Page<Album> page = albumService.page(new Page<>(pageNum, pageSize), queryWrapper);
//        List<Album> records = page.getRecords();
//        for (Album record : records) {
//            User user = userService.getById(record.getUserId());
//            if (user != null) {
//                record.setUsername(user.getNickname());
//            }
//        }
        Page<Album> page = albumService.findPage(new Page<>(pageNum, pageSize), name);
        return Result.success(page);
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response,HttpServletRequest req) throws Exception {
        // 从数据库查询出所有的数据
        List<Album> list = albumService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Album信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
        recordService.addRecord(req, Operation.exportAlbum.getName(), 1,getUser().getId());

        }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(HttpServletRequest req,MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Album> list = reader.readAll(Album.class);

        albumService.saveBatch(list);
        recordService.addRecord(req, Operation.importAlbum.getName(), 1,getUser().getId());
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

