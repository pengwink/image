package com.example.springboot.controller;

import cn.hutool.core.lang.UUID;
import com.example.springboot.common.Constant;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.exception.ServiceException;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @PostMapping("/file")
    @ApiOperation("上传图片")
    public Result upLoad(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) {
        //获取上传文件的名字
        String fileName = file.getOriginalFilename();
        //获取上传文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称UUID
        UUID uuid = UUID.randomUUID();
        //生成新的文件名称
        String fileNewName = uuid.toString() + suffixName;
        //上传图片的存放目录
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        //上传图片的存放路径
        File destFile = new File(Constant.FILE_UPLOAD_DIR + fileNewName);
        //判断目录是否存在
        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdir()) {//目录创建失败
                throw new ServiceException(Constants.CODE_200,"目录创建失败");
            }
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //httpServletRequest.getRequestURL():http://localhost:8083
            return Result.success(getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/file/"+ fileNewName);
        } catch (URISyntaxException e) {
            return Result.error(Constants.CODE_200,"目录创建失败");
        }
    }
    //获取URI：http://localhost:9090
    private URI getHost(URI uri) {
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
                    null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }

}
