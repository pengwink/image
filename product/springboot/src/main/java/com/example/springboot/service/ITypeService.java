package com.example.springboot.service;

import com.example.springboot.controller.dto.TypeDTO;
import com.example.springboot.entity.Photo;
import com.example.springboot.entity.Type;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-31
 */
public interface ITypeService extends IService<Type> {

    Type matche(String name);

    Type matcheType(String name);

    List<Type> findImage(Integer userId);

    List<Photo> findPhoto(Integer id,Integer userId);

    List<Type> typeAllPhoto();

    List<TypeDTO> listTypeDto();
}
