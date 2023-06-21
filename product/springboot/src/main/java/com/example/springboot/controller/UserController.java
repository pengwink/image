package com.example.springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.config.interceptor.AuthAccess;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.controller.dto.UserPasswordDTO;
import com.example.springboot.entity.Operation;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.IUserService;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.service.RecordService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author
 * @since 2022-01-26
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${files.upload.path}")
    private String filesUploadPath;

    @Resource
    private IUserService userService;
    @Resource
    private RecordService recordService;
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        password = convertMD5(password);
        userDTO.setPassword(password);
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        UserDTO dto = userService.login(userDTO);
        return Result.success(dto);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        password = convertMD5(password);
        userDTO.setPassword(password);
        String phone = userDTO.getPhone();
        String email = userDTO.getEmail();
        System.out.println(userDTO);
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        userDTO.setNickname(userDTO.getUsername());
        return Result.success(userService.register(userDTO));
    }

     // 新增或者更新
    @PostMapping
    public Result save(HttpServletRequest req, @RequestBody User user) {
        String username = user.getUsername();
        if (StrUtil.isBlank(username)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        if (StrUtil.isBlank(user.getNickname())) {
            user.setNickname(username);
        }
        if (user.getId() != null) {
            user.setPassword(null);
        } else {
            if (user.getPassword() == null) {
                user.setPassword("123");
            }
        }
        recordService.addRecord(req, Operation.addOrUpdateUser.getName()+"\""+user.getUsername()+"\"", 1,getUser().getId());
        return Result.success(userService.saveOrUpdate(user));
    }
    /**
     * 修改密码
     *
     * @param userPasswordDTO
     * @return
     */
    @PostMapping("/password")
    public Result password(HttpServletRequest req,@RequestBody UserPasswordDTO userPasswordDTO) {
        userService.updatePassword(userPasswordDTO);
        recordService.addRecord(req, Operation.updatePassword.getName(), 1,getUser().getId());
        return Result.success();
    }

    @AuthAccess
    @PutMapping("/reset")
    public Result reset(@RequestBody UserPasswordDTO userPasswordDTO) {
        if (StrUtil.isBlank(userPasswordDTO.getUsername()) || StrUtil.isBlank(userPasswordDTO.getPhone())) {
            throw new ServiceException("-1", "参数异常");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userPasswordDTO.getUsername());
        queryWrapper.eq("phone", userPasswordDTO.getPhone());
        List<User> list = userService.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            User user = list.get(0);
            user.setPassword("123");
            userService.updateById(user);
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(HttpServletRequest req,@PathVariable Integer id) {
        userService.removeById(id);
        recordService.addRecord(req, Operation.deleteUser.getName(), 1,getUser().getId());
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(HttpServletRequest req,@RequestBody List<Integer> ids) {
        userService.removeByIds(ids);
        recordService.addRecord(req, Operation.deletebatchUser.getName(), 1,getUser().getId());
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String nickname,
                           @RequestParam(defaultValue = "") String email
                           ) {

//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByAsc("id");
//        if (!"".equals(username)) {
//            queryWrapper.like("username", username);
//        }
//        if (!"".equals(email)) {
//            queryWrapper.like("email", email);
//        }

        return Result.success(userService.findPage(new Page<>(pageNum, pageSize), username,nickname,email));
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response,HttpServletRequest req) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avatarUrl", "头像");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
        recordService.addRecord(req, Operation.exportUser.getName(), 1,getUser().getId());

    }

    /**
     * excel 导入
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file,HttpServletRequest req) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAvatarUrl(row.get(6).toString());
            users.add(user);
        }

        userService.saveBatch(users);
        recordService.addRecord(req, Operation.exportUser.getName(), 1,getUser().getId());
        return Result.success(true);
    }
    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

    /**
     * 使用md5的算法进行加密
     */
    public static String md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    /**
     * 可逆的的加密解密方法；两次是解密，一次是加密
     * @param inStr
     * @return
     */
    public static String convertMD5(String inStr){

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++){
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }
}

