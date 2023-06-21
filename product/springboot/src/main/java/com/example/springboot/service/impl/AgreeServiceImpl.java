package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.entity.Agree;
import com.example.springboot.mapper.AgreeMapper;
import com.example.springboot.service.IAgreeService;
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
public class AgreeServiceImpl extends ServiceImpl<AgreeMapper, Agree> implements IAgreeService {

    @Resource
    AgreeMapper agreeMapper;
    @Override
    public Agree sellectAgree(Agree agree) {
        QueryWrapper<Agree> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",agree.getUserId());
        queryWrapper.eq("photo_id",agree.getPhotoId());
        return agreeMapper.selectOne(queryWrapper);
    }
}
