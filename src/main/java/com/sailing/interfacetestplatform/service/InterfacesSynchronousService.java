package com.sailing.interfacetestplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sailing.interfacetestplatform.entity.InterfaceEntity;

/**
 * @auther:张启航Sailling
 * @createDate:2022/9/29 16:24
 * @description:接口同步任务接口
 **/
public interface InterfacesSynchronousService extends IService<InterfaceEntity> {
    /**
     *  导入swagger内 目前项目不存在的接口
     */
//    ResponseData<Boolean> addSwaggerInterfaces(TaskRunInputDto taskRunInputDto);
}
