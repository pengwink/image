package com.example.springboot.entity;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.springboot.mapper.TypeMapper;
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
 * @since 2023-03-31
 */
@Getter
@Setter
@ApiModel(value = "Type对象", description = "")
public class Type implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("分类级别")
    private String level;

    @ApiModelProperty("父节点")
    private Integer parentId;

    @TableField(exist = false)
    private boolean flag=false;

    @TableField(exist = false)
    private Integer value;

}
