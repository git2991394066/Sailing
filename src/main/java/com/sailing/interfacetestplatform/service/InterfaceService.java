package com.sailing.interfacetestplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.interf.InterfaceCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.interf.InterfaceUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.interf.InterfaceOutputDto;
import com.sailing.interfacetestplatform.dto.output.interf.InterfaceQueryOutputDto;
import com.sailing.interfacetestplatform.entity.InterfaceEntity;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/18/0018 0:49:26
 * @description:项目接口服务接口
 **/
public interface InterfaceService extends IService<InterfaceEntity> {
    /**
     * 分页查询列表
     * @param pageIndex
     * @param pageSize
     * * @param moduleId
     * @param projectId
     * @return
     */
    ResponseData<List<InterfaceQueryOutputDto>> query(Integer pageIndex, Integer pageSize, Integer moduleId, Integer projectId);

    /**
     * 查询所有
     * @return
     */
    ResponseData<List<InterfaceOutputDto>> queryByProjectId(Integer projectId);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    ResponseData<InterfaceOutputDto> getById(Integer id);

    /**
     * 创建
     * @param inputDto
     * @return
     */
    ResponseData<InterfaceOutputDto> create(InterfaceCreateInputDto inputDto);

    /**
     * 更新
     * @param inputDto
     * @return
     */
    ResponseData<InterfaceOutputDto> update(InterfaceUpdateInputDto inputDto);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    ResponseData<Boolean> delete(Integer id);
}
