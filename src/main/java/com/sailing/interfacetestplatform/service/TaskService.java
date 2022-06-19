package com.sailing.interfacetestplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.output.module.ModuleOutputDto;
import com.sailing.interfacetestplatform.dto.output.task.TaskDetailOutputDto;
import com.sailing.interfacetestplatform.dto.output.task.TaskOutputDto;
import com.sailing.interfacetestplatform.entity.TaskEntity;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:32:20
 * @description:测试任务服务接口
 **/
public interface TaskService extends IService<TaskEntity> {
//    /**
//     * 分页查询列表
//     * @param pageIndex
//     * @param pageSize
//     * @param projectId
//     * @return
//     */
//    ResponseData<List<TaskQueryOutputDto>> query(Integer pageIndex, Integer pageSize, String name, Integer projectId);

    /**
     * 查询全部
     * @return
     */
    ResponseData<List<TaskOutputDto>> queryByProjectId(Integer projectId);

    /**
     * 查询全部，包含详情
     * @return
     */
    ResponseData<List<TaskDetailOutputDto>> queryDetailByProjectId(Integer projectId);
//
//    /**
//     * 根据id获取
//     * @param id
//     * @return
//     */
//    ResponseData<TaskOutputDto> getById(Integer id);
//
//    /**
//     * 创建
//     * @param inputDto
//     * @return
//     */
//    ResponseData<TaskOutputDto> create(TaskCreateInputDto inputDto);
//
//    /**
//     * 更新
//     * @param inputDto
//     * @return
//     */
//    ResponseData<TaskOutputDto> update(TaskUpdateInputDto inputDto);
//
//    /**
//     * 根据id删除
//     * @param id
//     * @return
//     */
//    ResponseData<Boolean> delete(Integer id);
//
    /**
     * 根据任务ID获取关联模块
     * @param taskId
     * @return
     */
    ResponseData<List<ModuleOutputDto>> getModulesByTaskId(Integer taskId);
//
//    /**
//     * 运行任务
//     * @param taskRunInputDto
//     * @return
//     */
//    ResponseData<Boolean> run(TaskRunInputDto taskRunInputDto);
//
//    /**
//     * 归档任务
//     * @param id
//     * @return
//     */
//    ResponseData<Boolean> archive(Integer id);
}

