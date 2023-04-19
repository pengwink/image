package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
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
 * @since 2023-03-30
 */
@Getter
@Setter
@ApiModel(value = "Album对象", description = "")
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("相册编号 主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("所属用户编号 外键")
    private Integer userId;

    @ApiModelProperty("相册名称")
    private String name;

    @ApiModelProperty("相册状态")
    private Integer statue;

    @ApiModelProperty("相册权限")
    private Integer righte;

    @ApiModelProperty("相册创建时间")
    private String creatTime;
    //    private LocalDateTime creatTime;
    @ApiModelProperty("相册封面")
    private String albumImg;

    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private Integer imageNumber;

    @TableField(exist = false)
    private String time;
    @TableField(exist = false)
    List<Photo> image;

    public Album(String time, List<Photo> image) {
        this.time = time;
        this.image = image;
    }

    public Album() {

    }
    public List<Photo> getImage() {
        return image;
    }
}
