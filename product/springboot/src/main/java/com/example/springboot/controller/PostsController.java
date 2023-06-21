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
import com.example.springboot.entity.*;
import com.example.springboot.service.RecordService;
import com.example.springboot.utils.FileServeUtil;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IPostsService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-04-19
 */
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Resource
    private FileServeUtil fileServeUtil;
    @Resource
    private RecordService recordService;
    private final String now = DateUtil.now();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Resource
    private IPostsService postsService;
    @RequestMapping("/upload")
    public JSONObject upload(HttpServletRequest req, String name, String descr, String typeName,String weight,String height, @RequestParam("file")MultipartFile[] multipartFiles,@RequestParam("imageId") List<Integer> imageId) throws Exception {
        System.out.println(imageId);
        Integer hideRadio = 1;
        Integer isPass = 0;
        System.out.println(name + "," + descr +","+typeName+","+weight);
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
        String imgDate =formatter.format(date);
        List<Posts> imgList = new ArrayList<>();
        //将所有上传的图片对象存入集合
        for (MultipartFile m:multipartFiles){
            String fileName = m.getOriginalFilename();
            String imageUrL = fileServeUtil.uploadServe(req,m);
            imgList.add(new Posts(null,name,descr,imageUrL,imgDate,userId,hideRadio,isPass));
        }
        postsService.addPosts(req,imgList,typeName,imageId);
        if(true){
            jsonObject.put("status","success");
            //记录操作
            recordService.addRecord(req, Operation.addFabu.getName(),imgList.size(),userId);
        }
        else{
            for(Posts i:imgList){
                String imageUrL = i.getImg();
                fileServeUtil.deleteServe(imageUrL);
            }
            jsonObject.put("status","fail");
        }
        return jsonObject;
    }
    //    // 新增或者更新
    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Posts posts) {
        if (posts.getId() == null) {
            //posts.setTime(DateUtil.now());
            //posts.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        postsService.saveOrUpdate(posts);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        postsService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        postsService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Posts> list = postsService.selectAl();
        System.out.println(list);
        return Result.success(list);
    }
    @GetMapping("/selectAlll")
    public Result selectAlll(@RequestParam Integer id,@RequestParam Integer uid) {
        List<Photo> list = postsService.selectAlll(id,uid);
        System.out.println(list);
        return Result.success(list);
    }
    /**
     *分页查询所有
     * @return
     */
    @RequestMapping("/selectAl")
    public JSONObject selectAllImage(Integer currentPage,Integer pageSize,Integer userId){
        JSONObject jsonObject = new JSONObject();
        if(userId !=null) {
            PostsEnca post = postsService.selectPostPage(userId,currentPage,pageSize);
            jsonObject.put("data",post);
            jsonObject.put("status","success");
        }
        else{
            jsonObject.put("status","fail");
        }
        return jsonObject;
    }
    @GetMapping
    public Result findAll() {
        return Result.success(postsService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(postsService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        return Result.success(postsService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Posts> list = postsService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Posts信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

        }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Posts> list = reader.readAll(Posts.class);

        postsService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

