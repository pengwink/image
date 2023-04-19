package com.example.springboot.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @program: MemoryBack
 * @description 返回所有图片的时间和类型结果
 * @author: xiaohua
 * @create: 2022-09-02 14:00
 **/
public class AllTimeType {
    private List<String> imageType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private List<String> imageDate;
    public AllTimeType(){
    }
    public AllTimeType(List<String> imageType, List<String> imageDate) {
        this.imageType = imageType;
        this.imageDate = imageDate;
    }

    public List<String> getImageType() {
        return imageType;
    }

    public void setImageType(List<String> imageType) {
        this.imageType = imageType;
    }

    public List<String> getImageDate() {
        return imageDate;
    }

    public void setImageDate(List<String> imageDate) {
        this.imageDate = imageDate;
    }

    @Override
    public String toString() {
        return "AllTimeTypeVO{" +
                "imageType=" + imageType +
                ", imageDate=" + imageDate +
                '}';
    }
}
