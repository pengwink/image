package com.example.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.RoleEnum;
import com.example.springboot.controller.UserController;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.controller.dto.UserPasswordDTO;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.common.Constants;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.example.springboot.controller.UserController.convertMD5;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author
 * @since 2022-01-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, userDTO, true);
            // 设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);

            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public User register(UserDTO userDTO) {
        User one = getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, userDTO.getUsername()));
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            // 默认一个普通用户的角色
            one.setRoles(RoleEnum.ROLE_USER.toString());
            save(one);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }

    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        int update = userMapper.updatePassword(userPasswordDTO);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }

    @Override
    public Page<User> findPage(Page<User> Page, String username, String nickname, String email) {
        return userMapper.findPage(Page, username, nickname, email);
    }

    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    /**
//     * 获取当前角色的菜单列表
//     * @param roleFlag
//     * @return
//     */
//    private List<Menu> getRoleMenus(String roleFlag) {
//        Integer roleId = roleMapper.selectByFlag(roleFlag);
//        // 当前角色的所有菜单id集合
//        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
//
//        // 查出系统所有的菜单(树形)
//        List<Menu> menus = menuService.findMenus("");
//        // new一个最后筛选完成之后的list
//        List<Menu> roleMenus = new ArrayList<>();
//        // 筛选当前用户角色的菜单
//        for (Menu menu : menus) {
//            if (menuIds.contains(menu.getId())) {
//                roleMenus.add(menu);
//            }
//            List<Menu> children = menu.getChildren();
//            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
//            children.removeIf(child -> !menuIds.contains(child.getId()));
//        }
//        return roleMenus;
//    }

}
