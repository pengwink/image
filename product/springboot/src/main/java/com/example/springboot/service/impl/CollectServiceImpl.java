package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.entity.Agree;
import com.example.springboot.entity.Collect;
import com.example.springboot.mapper.AgreeMapper;
import com.example.springboot.mapper.CollectMapper;
import com.example.springboot.service.ICollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-04-05
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements ICollectService {
    @Resource
    CollectMapper collectMapper;
    @Override
    public Collect sellectCollect(Collect collect) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",collect.getUserId());
        queryWrapper.eq("photo_id",collect.getPhotoId());
        return collectMapper.selectOne(queryWrapper);
    }
}
