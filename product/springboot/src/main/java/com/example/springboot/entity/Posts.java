package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

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
 * @since 2023-04-19
 */
@Getter
@Setter
@ApiModel(value = "Posts对象", description = "")
@NoArgsConstructor
public class Posts  implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("标题")
    private String name;

    @ApiModelProperty("描述")
    private String descr;

    @ApiModelProperty("封面")
    private String img;

    @ApiModelProperty("时间")
    private String time;

    @ApiModelProperty("发帖人")
    private Integer userId;

    @ApiModelProperty("是否显示")
    private Integer hideRadio;

    @ApiModelProperty("审核通过")
    private Integer isPass;
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String headImage;
    @TableField(exist = false)
    private List<Categories> Pcategories;
    public Posts(Integer id, String name, String descr, String imageUrL, String imgDate, Integer userId, Integer hideRadio, Integer isPass) {
        this.id = id;
        this.name = name;
        this.descr = descr;
        this.img = imageUrL;
        this.time = imgDate;
        this.userId = userId;
        this.hideRadio = hideRadio;
        this.isPass = isPass;
    }
}
