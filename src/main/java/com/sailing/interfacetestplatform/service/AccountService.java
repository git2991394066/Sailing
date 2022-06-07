package com.sailing.interfacetestplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.account.LoginInputDto;
import com.sailing.interfacetestplatform.dto.input.account.RegisterDto;
import com.sailing.interfacetestplatform.dto.output.account.LoginOutputDto;
import com.sailing.interfacetestplatform.entity.UserEntity;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 22:46:55
 * @description:账户服务
 **/
public interface AccountService extends IService<UserEntity> {
    /**
     * 登录
     * @param loginDto
     * @return
     */
    ResponseData<LoginOutputDto> login(LoginInputDto loginDto);

    /**
     * 注册
     * @param registerDto
     * @return
     */
    ResponseData<Boolean> register(RegisterDto registerDto);
}
