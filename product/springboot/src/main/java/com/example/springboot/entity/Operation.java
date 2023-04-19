package com.example.springboot.entity;

/**
 * @program: MemoryBack
 * @description 操作记录枚举
 * @author: xiaohua
 * @create: 2022-09-02 14:00
 **/
public enum Operation {
    uploadImage("上传图片",1),
    AIUploadImage("AI识别上传",2),
    downloadImage("下载图片",3),
    deleteImage("删除图片",4),
    addRecycle("加入回收站",5),
    recoverImage("恢复图片",6),
    createAlbum("创建相册",7),
    deleteAlbum("删除相册",8),
    deleteBatchAlbum("删除相册",9),
    addImageToAlbum("添加图片至相册",10),
    deleteAlbumImage("相册移除图片",11),

    addOrUpdateUser("添加和编辑用户",12),
    deleteUser("删除用户",13),
    deletebatchUser("批量删除用户",14),
    exportUser("导出用户信息",15),
    importUser("导入用户信息",16),
    exportAlbum("导出相册信息",17),
    importAlbum("导入相册信息",18),
    exportImage("导出图片信息",19),
    importImage("导入图片信息",20),
    addType("添加和更新类型",21),
    deleteType("删除类型",22),
    importType("导入类型",23),
    exportType("导入类型",24),
    updatePassword("修改密码",25);
    private String name;
    private Integer number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    Operation(String name, Integer number) {
        this.name = name;
        this.number = number;
    }
}
