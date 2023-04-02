package com.example.springboot.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 存放常量
 */
@Component
public class Constant {
    //public static final String IMOOC_MALL_USER="imooc_mall_user";//用户存放在session中的key

    //public static final String SALT="sdcrvgtn-2k";//MD5加密的盐值

    //@Value("${file.upload.dir}")  //由于下面的这个属性是静态的，不能直接使用@Value注解，需要使用set方法设置
    //public static String FILE_UPLOAD_DIR;//设置上传图片的存储路径


    public static String FILE_UPLOAD_DIR;//设置上传图片的存储路径
    @Value("${files.upload.path}") //需要spring注入这个value，因此需要创建bean，使用@component注解
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir + "ddd/";
        System.out.println(FILE_UPLOAD_DIR);
    }
}
