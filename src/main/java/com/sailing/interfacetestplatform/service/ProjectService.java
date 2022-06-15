package com.sailing.interfacetestplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.project.ProjectCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.project.ProjectUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.project.ProjectOutputDto;
import com.sailing.interfacetestplatform.entity.ProjectEntity;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/15/0015 23:18:36
 * @description:项目服务接口
 **/
public interface ProjectService extends IService<ProjectEntity> {
    /**
     * 查询所有
     * @return
     */
    ResponseData<List<ProjectOutputDto>> queryAll();

    /**
     * 根据id获取项目
     * @param id
     * @return
     */
    ResponseData<ProjectOutputDto> getById(Integer id);

    /**
     * 创建项目
     * @param projectCreateInputDto
     * @return
     */
    ResponseData<ProjectOutputDto> create(ProjectCreateInputDto projectCreateInputDto);

    /**
     * 更新项目
     * @param projectUpdateInputDto
     * @return
     */
    ResponseData<ProjectOutputDto> update(ProjectUpdateInputDto projectUpdateInputDto);

    /**
     * 根据id删除项目
     * @param id
     * @return
     */
    ResponseData<Boolean> delete(Integer id);
}

