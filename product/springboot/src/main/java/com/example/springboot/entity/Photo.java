package com.example.springboot.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-31
 */
@Getter
@Setter
@ApiModel(value = "Photo对象", description = "")
@NoArgsConstructor
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("图片编号 主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("相册编号 外键")
    private Integer albumId;

    @ApiModelProperty("所属用户id 外键")
    private Integer userId;

    @ApiModelProperty("类型id")
    private Integer typeId;

    @ApiModelProperty("图片名称")
    private String name;

    @ApiModelProperty("图片")
    private String img;

    @ApiModelProperty("图片详情")
    private String content;

    @ApiModelProperty("图片创建时间")
    private String photoTime;

    @ApiModelProperty("图片权限 1可访问、0不可访问")
    private Integer photoRight;

    @ApiModelProperty("图片状态 1有效、0禁用")
    private Integer photoStatue;

    @ApiModelProperty("是否符合分类规范")
    private Integer isStandard;

    @TableField(exist = false)
    private String albumName;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String userNickname;
    @TableField(exist = false)
    private String typeName;
    @TableField(exist = false)
    private Integer isAgree;

    @TableField(exist = false)
    private Integer isCollect;
    @TableField(exist = false)
    private Integer imageId;
    @TableField(exist = false)
    private Integer collectId;
    @TableField(exist = false)
    private Integer agreeId;

    public Photo(Integer id, Integer albumId, Integer userId, Integer typeId, String name, String imageUrL, String content, String imgDate, Integer photoRight, Integer photoStatue, Integer isStandrd) {
        this.id = id;
        this.albumId = albumId;
        this.userId = userId;
        this.typeId = typeId;
        this.name = name;
        this.img = imageUrL;
        this.content = content;
        this.photoTime = imgDate;
        this.photoRight = photoRight;
        this.photoStatue = photoStatue;
        this.isStandard = isStandrd;
    }
}
