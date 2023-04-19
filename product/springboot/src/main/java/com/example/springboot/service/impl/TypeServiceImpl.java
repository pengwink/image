package com.example.springboot.service.impl;

import com.example.springboot.entity.Photo;
import com.example.springboot.entity.Type;
import com.example.springboot.mapper.TypeMapper;
import com.example.springboot.service.ITypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-31
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements ITypeService {

    @Resource
    private TypeMapper typeMapper;
    @Override
    public Type matche(String name) {
        return typeMapper.matche(name);
    }

    @Override
    public Type matcheType(String name) {
        return typeMapper.matcheType(name);
    }

    @Override
    public List<Type> findImage(Integer userId) {
        List<Type> result = typeMapper.findImage(userId);
        return result;
    }

    @Override
    public List<Photo> findPhoto(Integer id,Integer userId) {
        return typeMapper.findPhoto(id,userId);
    }

    @Override
    public List<Type> typeAllPhoto() {
        return typeMapper.typeAllPhoto();
    }
}
