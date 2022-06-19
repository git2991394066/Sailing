package com.sailing.interfacetestplatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sailing.interfacetestplatform.entity.TaskModuleEntity;
import com.sailing.interfacetestplatform.mapper.TaskModuleMapper;
import com.sailing.interfacetestplatform.service.TaskModuleService;
import org.springframework.stereotype.Service;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 1:25:49
 * @description:测试任务-模块服务实现
 **/
@Service
public class TaskModuleServiceImpl extends ServiceImpl<TaskModuleMapper, TaskModuleEntity> implements TaskModuleService {
}