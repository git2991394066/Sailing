package com.sailing.interfacetestplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.environment.EnvironmentCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.environment.EnvironmentUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.environment.EnvironmentOutputDto;
import com.sailing.interfacetestplatform.entity.EnvironmentEntity;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:31:28
 * @description:项目环境服务接口
 **/
public interface EnvironmentService extends IService<EnvironmentEntity> {
    /**
     * 查询所有
     * @return
     */
    ResponseData<List<EnvironmentOutputDto>> queryByProjectId(Integer projectId);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    ResponseData<EnvironmentOutputDto> getById(Integer id);

    /**
     * 创建
     * @param inputDto
     * @return
     */
    ResponseData<EnvironmentOutputDto> create(EnvironmentCreateInputDto inputDto);

    /**
     * 更新
     * @param inputDto
     * @return
     */
    ResponseData<EnvironmentOutputDto> update(EnvironmentUpdateInputDto inputDto);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    ResponseData<Boolean> delete(Integer id);
}

