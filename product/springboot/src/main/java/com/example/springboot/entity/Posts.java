package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
 * @since 2023-04-19
 */
@Getter
@Setter
@ApiModel(value = "Posts对象", description = "")
public class Posts implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("标题")
    private String name;

    @ApiModelProperty("描述")
    private String descr;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("封面")
    private String img;

    @ApiModelProperty("时间")
    private String time;

    @ApiModelProperty("发帖人")
    private Integer userId;

    @ApiModelProperty("是否显示")
    private String hideRadio;

    @ApiModelProperty("审核通过")
    private Integer isPass;
}
