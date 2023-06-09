package com.example.springboot.service;

import com.example.springboot.entity.Collect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 坚强的小喵
 * @since 2023-04-05
 */
public interface ICollectService extends IService<Collect> {

    Collect sellectCollect(Collect collect);
}
