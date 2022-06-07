package com.sailing.interfacetestplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.user.UserCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.user.UserResetPasswordInputDto;
import com.sailing.interfacetestplatform.dto.input.user.UserUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.user.UserOutputDto;
import com.sailing.interfacetestplatform.entity.UserEntity;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 22:43:53
 * @description:用户服务
 **/
public interface UserService extends IService<UserEntity> {
    /**
     * 分页查询
     * @param pageIndex
     * @param pageSize
     * @param username
     * @param name
     * @return
     */
    ResponseData<List<UserOutputDto>> query(Integer pageIndex, Integer pageSize, String username, String name);

    /**
     * 查询所有数据
     * @return
     */
    ResponseData<List<UserOutputDto>> queryAll();

    /**
     * 根据Id获取
     * @param id
     * @return
     */
    ResponseData<UserOutputDto> getById(Integer id);

    /**
     * 创建
     * @param inputDto
     * @return
     */
    ResponseData<UserOutputDto> create(UserCreateInputDto inputDto);

    /**
     * 更新
     * @param inputDto
     * @return
     */
    ResponseData<UserOutputDto> update(UserUpdateInputDto inputDto);

    /**
     * 删除
     * @param id
     */
    ResponseData<Boolean> delete(Integer id);

    /**
     * 重置密码
     * @param inputDto
     * @return
     */
    ResponseData<Boolean> resetPassord(UserResetPasswordInputDto inputDto);
}
