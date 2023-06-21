package com.example.springboot.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Constant;
import com.example.springboot.entity.Files;
import com.example.springboot.mapper.FileMapper;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class FileServeUtil {
    @Value("${files.upload.path}")
    private String uploadPath;
    private String uploadPrefix = "http://localhost:9090/file/";
    private String originPrefix = "/origin/";
    private String compressPrefix = "/compress/";
    float limitSize = 3145728;
    float minSize =   100000;
    @Resource
    private FileMapper fileMapper;
    private static final String APP_ID = "34345125";
    private static final String API_KEY  = "8p5rM5tcfkoEj0M9vilC5w5Z";
    private static final String SECRET_KEY = "MXfG45i2NiGUNcuRG550CQLsSFiDGcrU";
    public static void main(String[] args) {
        FileServeUtil fileServeUtil = new FileServeUtil();
        fileServeUtil.deleteServe("/serve/img/xiaofeng/1.jpg");
    }

    /**
     * 服务器存储路径转为绝对路径
     * @param ServePath
     * @return
     */
    public String ServPathToAP(String ServePath){
        String AbsolutePath = "";
        String path = ServePath.substring(uploadPrefix.length());
        AbsolutePath = uploadPath+"/ddd/"+path;
        return AbsolutePath;
    }



    /**
     * 计算每个文件的UUId
     * @param file
     * @return
     */
    public String getMD5(File file) {
        FileInputStream fileInputStream = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
//            采用时间加MD5方式保证每个文件的唯一性
            String md5 = new String(Hex.encodeHex(MD5.digest()));
//            Date date = new Date();
//            long time = date.getTime();
//            String str = md5+time;
            return md5;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param multipartFile 文件流
     * @return 数据库中服务器文件的存储路径
     * @throws IOException
     */
    public String uploadServe(HttpServletRequest req,MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = multipartFile.getSize();
        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;
        File uploadFile = new File(Constant.FILE_UPLOAD_DIR+ fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String url;
        // 获取文件的md5
        String md5 = SecureUtil.md5(multipartFile.getInputStream());
        // 从数据库查询是否存在相同的记录
        Files dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
        } else {
            // 上传文件到磁盘
            multipartFile.transferTo(uploadFile);
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/file/"+ fileUUID;
        }
        AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);
//        String bendi = "E:\\Data\\images\\ddd\\"+fileUUID;
//        org.json.JSONObject res = client.imageCensorUserDefined(bendi, EImgType.FILE,null);
//        System.out.println(res.toString());
//        if(res.getInt("conclusionType")!=1){
//            return "fail";
//        }
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024); // 单位 kb
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);
        return url;
    }

    /**
     * 删除服务器文件
     * @param filePath
     */
    public void deleteServe(String filePath){
        String ApPath = ServPathToAP(filePath);
        new File(ApPath).delete();
    }

    public String CompressImage(String ApPath,Float imgSize) throws IOException {
        String MidPath= "ddd"+compressPrefix;
        File tempFile = new File(ApPath);
        if(!tempFile.getParentFile().exists()){
            tempFile.getParentFile().mkdirs();
        }
        //压缩比率
        float imgRate = limitSize>imgSize?(float) 0.9:(limitSize)/imgSize;

        String suffix = ApPath.substring(ApPath.lastIndexOf("/"));
        File imgFile = new File(uploadPath+MidPath+suffix);
        if(!imgFile.getParentFile().exists()){
            imgFile.getParentFile().mkdirs();
        }
//        最小100kb，否则不能压缩
        if(imgSize<minSize){
            copyFileUsingStream(new File(ApPath), new File(uploadPath + MidPath + suffix));
        }
        else{
            Thumbnails.of(ApPath).scale(1f).outputQuality(imgRate).toFile(uploadPath+MidPath+suffix);
        }

        return  uploadPrefix+MidPath+suffix;
    }
//    复制文件
    public void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    /**
     * 通过文件的md5查询文件
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }
}

