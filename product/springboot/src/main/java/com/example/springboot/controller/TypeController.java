package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Operation;
import com.example.springboot.service.RecordService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.ITypeService;
import com.example.springboot.entity.Type;

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
@RequestMapping("/type")
public class TypeController {

    @Resource
    private ITypeService typeService;

    private final String now = DateUtil.now();
    @Resource
    private RecordService recordService;
    // 新增或者更新
    @PostMapping
    public Result save(HttpServletRequest req, @RequestBody Type type) {
        if (type.getId() == null) {
            //type.setTime(DateUtil.now());
            //type.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        recordService.addRecord(req, Operation.addType.getName(), 1,getUser().getId());
        typeService.saveOrUpdate(type);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(HttpServletRequest req,@PathVariable Integer id) {
        typeService.removeById(id);
        recordService.addRecord(req, Operation.deleteType.getName(), 1,getUser().getId());
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(HttpServletRequest req,@RequestBody List<Integer> ids) {
        typeService.removeByIds(ids);
        recordService.addRecord(req, Operation.deleteType.getName(), 1,getUser().getId());
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(typeService.list());
    }

    /**
     * 查找该用户下的所有类型
     * @return
     */
    @GetMapping("/photos")
    public Result findImage() {
        return Result.success(typeService.findImage(getUser().getId()));
    }

    /**
     *查找该用户下指定类型的图片
     * @param id
     * @return
     */
    @GetMapping("/photos/{id}")
    public Result findPhoto(@PathVariable Integer id) {
        return Result.success(typeService.findPhoto(id,getUser().getId()));
    }

    /**
     * 查找所有图片的所有类型和数量
     * @return
     */
    @PostMapping("/typeAllPhoto")
    public Result typeAllPhoto() {

        return Result.success( typeService.typeAllPhoto());
    }


    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(typeService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Type> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }

        return Result.success(typeService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response,HttpServletRequest req) throws Exception {
        // 从数据库查询出所有的数据
        List<Type> list = typeService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Type信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
        recordService.addRecord(req, Operation.deleteType.getName(), 1,getUser().getId());

        }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file,HttpServletRequest req) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Type> list = reader.readAll(Type.class);

        typeService.saveBatch(list);
        recordService.addRecord(req, Operation.deleteType.getName(), 1,getUser().getId());

        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

