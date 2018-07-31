package com.santo.service.impl;

import com.santo.base.Constant;
import com.santo.entity.User;
import com.santo.entity.UserThirdparty;
import com.santo.mapper.UserThirdpartyMapper;
import com.santo.model.ThirdPartyUser;
import com.santo.service.IUserService;
import com.santo.service.IUserThirdpartyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 第三方用户表 服务实现类
 * </p>
 *
 * @author huliangjun
 * @since 2018-07-27
 */
@Service
public class UserThirdpartyServiceImpl extends ServiceImpl<UserThirdpartyMapper, UserThirdparty> implements IUserThirdpartyService {

    @Autowired
    private IUserService userService;

    @Override
    public User insertThirdPartyUser(ThirdPartyUser param, String password) throws Exception{
        User sysUser = new User();
        sysUser.setPassWord(password);
        sysUser.setUserName("游客"+param.getOpenid());
        sysUser.setMobile(param.getOpenid());
        sysUser.setAvatar(param.getAvatarUrl());
        User register = userService.register(sysUser, Constant.RoleType.USER);
        // 初始化第三方信息
        UserThirdparty thirdparty = new UserThirdparty();
        thirdparty.setProviderType(param.getProvider());
        thirdparty.setOpenId(param.getOpenid());
        thirdparty.setCreateTime(System.currentTimeMillis());
        thirdparty.setUserNo(register.getUserNo());
        thirdparty.setStatus(Constant.ENABLE);
        thirdparty.setAccessToken(param.getToken());
        this.insert(thirdparty);
        return register;
    }
}
