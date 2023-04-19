package com.example.springboot.entity;

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
 * @since 2023-04-05
 */
@Getter
@Setter
@ApiModel(value = "Agree对象", description = "")
public class Agree implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("点赞id 主键")
    private Integer id;

    @ApiModelProperty("用户id 外键")
    private Integer userId;

    @ApiModelProperty("图片id 外键")
    private Integer photoId;

    @ApiModelProperty("是否点赞")
    private Integer isAgree;


}
