package com.example.springboot.entity;


import java.util.List;

/**
 * @program: MemoryBack
 * @description 返回图片的压缩UrL和原品质UrL
 * @author: xiaohua
 * @create: 2022-09-02 14:00
 **/
public class ImageEnca {
    private List<Photo> images;
    private List<String> previewImageUrL;
    private Integer totalCount;

    @Override
    public String toString() {
        return "ImageVO{" +
                "images=" + images +
                ", previewImageUrL=" + previewImageUrL +
                ", totalCount=" + totalCount +
                '}';
    }

    public List<Photo> getImages() {
        return images;
    }

    public void setImages(List<Photo> images) {
        this.images = images;
    }

    public List<String> getPreviewImageUrL() {
        return previewImageUrL;
    }

    public void setPreviewImageUrL(List<String> previewImageUrL) {
        this.previewImageUrL = previewImageUrL;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    public ImageEnca(){}
    public ImageEnca(List<Photo> images, List<String> previewImageUrL, Integer totalCount) {
        this.images = images;
        this.previewImageUrL = previewImageUrL;
        this.totalCount = totalCount;
    }
}
