package com.sailing.interfacetestplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.entity.ModuleEntity;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/9/29 16:37
 * @description:
 **/
public interface ModulesSynchronousService extends IService<ModuleEntity> {
    /**
     * 导入swagger内 目前项目不存在的模块
     */
    ResponseData<Boolean> addSwaggerModules(List<String> modulesName,Integer projectId);
}
