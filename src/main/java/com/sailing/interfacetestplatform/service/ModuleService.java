package com.sailing.interfacetestplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.module.ModuleCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.module.ModuleUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.module.ModuleOutputDto;
import com.sailing.interfacetestplatform.entity.ModuleEntity;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:31:52
 * @description:项目模块服务接口
 **/
public interface ModuleService extends IService<ModuleEntity> {
    /**
     * 分页查询列表
     * @param pageIndex
     * @param pageSize
     * @param projectId
     * @return
     */
    ResponseData<List<ModuleOutputDto>> query(Integer pageIndex, Integer pageSize, Integer projectId);

    /**
     * 查询全部
     * @return
     */
    ResponseData<List<ModuleOutputDto>> queryByProjectId(Integer projectId);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    ResponseData<ModuleOutputDto> getById(Integer id);

    /**
     * 创建
     * @param inputDto
     * @return
     */
    ResponseData<ModuleOutputDto> create(ModuleCreateInputDto inputDto);

    /**
     * 更新
     * @param inputDto
     * @return
     */
    ResponseData<ModuleOutputDto> update(ModuleUpdateInputDto inputDto);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    ResponseData<Boolean> delete(Integer id);
}
