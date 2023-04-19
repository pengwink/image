package com.example.springboot.controller.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 接受前端登录请求的参数
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String roles;
    private String email;

    private String phone;
}
