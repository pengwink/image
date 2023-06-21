package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-05-04
 */
@Getter
@Setter
@ApiModel(value = "Comment对象", description = "")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论的id 主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("用户id 外键")
    private Integer userId;

    @ApiModelProperty("评论时间")
    private String time;

    @ApiModelProperty("被评论的图片 外键")
    private Integer postsId;

    @ApiModelProperty("父级id")
    private Integer pid;
    @TableField(exist = false)
    private Integer pUserId;
    @TableField(exist = false)
    private String pNickname;

    @ApiModelProperty("原始id")
    private Integer originId;

    @TableField(exist = false)
    private String nickname;
    @TableField(exist = false)
    private String avatarUrl;
    @TableField(exist = false)
    private String pName;
    @TableField(exist = false)
    private List<Comment> children;
}
