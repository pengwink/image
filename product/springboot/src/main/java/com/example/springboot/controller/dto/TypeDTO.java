package com.example.springboot.controller.dto;

import lombok.Data;

import java.util.List;

/**
 * 接受前端登录请求的参数
 */
@Data
public class TypeDTO {
    private String value;
    private String label;
    List<TypeDTO> children;
}
