package com.example.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Photo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-03-31
 */
public interface PhotoMapper extends BaseMapper<Photo> {

    Page<Photo> findPage(Page<Photo> page, String name);
}
