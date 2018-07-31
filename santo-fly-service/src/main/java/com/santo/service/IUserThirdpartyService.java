package com.santo.service;

import com.santo.entity.User;
import com.santo.entity.UserThirdparty;
import com.baomidou.mybatisplus.service.IService;
import com.santo.model.ThirdPartyUser;

/**
 * <p>
 * 第三方用户表 服务类
 * </p>
 *
 * @author huliangjun
 * @since 2018-07-27
 */
public interface IUserThirdpartyService extends IService<UserThirdparty> {

    User insertThirdPartyUser(ThirdPartyUser param, String password)throws Exception;

}
