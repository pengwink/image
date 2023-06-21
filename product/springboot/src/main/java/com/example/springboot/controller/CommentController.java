package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Collect;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.ICommentService;
import com.example.springboot.entity.Comment;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-05-04
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private ICommentService commentService;

    private final String now = DateUtil.now();

    private static final String APP_ID = "34345125";
    private static final String API_KEY  = "8p5rM5tcfkoEj0M9vilC5w5Z";
    private static final String SECRET_KEY = "MXfG45i2NiGUNcuRG550CQLsSFiDGcrU";

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Comment comment) {
        // 初始化一个AipContentCensor
        AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);

//        // 可选：设置网络连接参数
//        client.setConnectionTimeoutInMillis(2000);
//        client.setSocketTimeoutInMillis(60000);


        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量

        // 调用接口
//        String path = "test.jpg";
//        JSONObject res = client.imageCensorUserDefined(path);
//        System.out.println(res.toString(2));

        if (comment.getId() == null) {
            comment.setTime(DateUtil.now());
            if(comment.getPid()!=null){
                Integer pid = comment.getPid();
                Comment pComment = commentService.getById(pid);
                if (pComment.getOriginId()!=null){
                    comment.setOriginId(pComment.getOriginId());
                }else{
                    comment.setOriginId(comment.getPid());
                }
            }
        }
        String text = comment.getContent();
        JSONObject response = client.textCensorUserDefined(text);
        System.out.println(response);
        if(response.getInt("conclusionType")!=1){
            return Result.error("600","内容有违规词汇");
        }
        commentService.saveOrUpdate(comment);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        commentService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        commentService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(commentService.list());
    }

    @GetMapping("/tree/{postsId}")
    public Result findTree(@PathVariable Integer postsId){
        List<Comment> postsComments = commentService.findCommentDetail(postsId);
        List<Comment> originList = postsComments.stream().filter(comment -> comment.getOriginId()==null).collect(Collectors.toList());
        for (Comment origin : originList){
            List<Comment> comments = postsComments.stream().filter(comment -> origin.getId().equals(comment.getOriginId())).collect(Collectors.toList());
            comments.forEach(comment -> {
                postsComments.stream().filter(c1 -> c1.getId().equals(comment.getPid())).findFirst().ifPresent(v->{
                    comment.setPUserId(v.getUserId());
                    comment.setPNickname(v.getNickname());
                });
            });
            origin.setChildren(comments);
        }
        return Result.success(originList);
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(commentService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        return Result.success(commentService.findpage(new Page<>(pageNum, pageSize),name));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Comment> list = commentService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Comment信息表", "UTF-8");
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
        List<Comment> list = reader.readAll(Comment.class);

        commentService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

