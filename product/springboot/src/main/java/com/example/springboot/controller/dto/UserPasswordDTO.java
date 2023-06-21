package com.example.springboot.controller.dto;

import com.example.springboot.service.impl.UserServiceImpl;
import lombok.Data;

@Data
public abstract class UserPasswordDTO extends UserServiceImpl {
    private String username;
    private String phone;
    private String password;
    private String newPassword;
}
