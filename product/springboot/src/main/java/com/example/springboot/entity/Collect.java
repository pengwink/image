package com.example.springboot.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value = "Collect对象", description = "")
public class Collect implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("点赞id 主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer photoId;

    private Integer isCollect;


}
