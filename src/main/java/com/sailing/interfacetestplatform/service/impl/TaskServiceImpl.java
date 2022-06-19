package com.sailing.interfacetestplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.output.module.ModuleOutputDto;
import com.sailing.interfacetestplatform.dto.output.task.TaskDetailOutputDto;
import com.sailing.interfacetestplatform.dto.output.task.TaskOutputDto;
import com.sailing.interfacetestplatform.dto.output.testcase.TestCaseOutputDto;
import com.sailing.interfacetestplatform.dto.output.testsuit.TestSuitDetailOutputDto;
import com.sailing.interfacetestplatform.entity.*;
import com.sailing.interfacetestplatform.mapper.TaskMapper;
import com.sailing.interfacetestplatform.service.*;
import com.sailing.interfacetestplatform.util.SessionUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:36:37
 * @description:测试任务服务实现
 **/
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, TaskEntity> implements TaskService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CacheManager cacheManager;
    @Autowired
    SessionUtil sessionUtil;
    @Autowired
    ProjectService projectService;
    @Autowired
    EnvironmentService environmentService;
    @Autowired
    ModuleService moduleService;
//    @Autowired
//    InterfaceService interfaceService;
    @Autowired
    @Lazy
    TestCaseService testCaseService;
//    @Autowired
//    TestRecordService testRecordService;
    @Autowired
    @Lazy
    TestSuitService testSuitService;
//
    @Autowired
    @Lazy
    TaskModuleService taskModuleService;
//    @Autowired
//    TestReportService reportService;
//    @Autowired
//    TaskTestService taskTestService;

//    @Override
//    public ResponseData<List<TaskQueryOutputDto>> query(Integer pageIndex, Integer pageSize, String name, Integer projectId) {
//        ResponseData<List<TaskQueryOutputDto>> responseData;
//
//        try {
//            QueryWrapper<TaskEntity> queryWrapper = new QueryWrapper<>();
//            if(name!=null){
//                queryWrapper.like("name", name);
//            }
//            if(projectId != null) {
//                queryWrapper.eq("project_id", projectId);
//            }
//            queryWrapper.eq("is_delete",false); //只取没有删除的
//            queryWrapper.orderByDesc("id");
//            IPage<TaskEntity> queryPage = new Page<>(pageIndex, pageSize);
//            queryPage = this.page(queryPage,queryWrapper);
//            List<TaskEntity> taskEntities = queryPage.getRecords();
//
//            //获取所有关联模块信息
//            QueryWrapper<TaskModuleEntity> taskModuleEntityQueryWrapper = new QueryWrapper<>();
//            List<Integer> taskIds = taskEntities.stream().map(s->s.getId()).collect(Collectors.toList());
//            taskModuleEntityQueryWrapper.in("task_id", taskIds);
//            List <TaskModuleEntity> taskModuleEntities = taskModuleService.list(taskModuleEntityQueryWrapper);
//            List<Integer> modudleIds = taskModuleEntities.stream().map(s->s.getModuleId()).collect(Collectors.toList());
//            //获取所有关联模块详情
//            QueryWrapper<ModuleEntity> moduleEntityQueryWrapper = new QueryWrapper<>();
//            moduleEntityQueryWrapper.in("id",modudleIds);
//            moduleEntityQueryWrapper.eq("is_delete",false);
//            List<ModuleEntity> moduleEntities = moduleService.list(moduleEntityQueryWrapper);
//
//            //Entity转Dto
//            List<TaskQueryOutputDto> outputDtos = taskEntities.stream().map(s->modelMapper.map(s, TaskQueryOutputDto.class)).collect(Collectors.toList());
//            outputDtos.stream().forEach(s->{
//                //获取当前任务关联模块
//                List<TaskModuleEntity> currentTaskModuleEntities = taskModuleEntities.stream().filter(t->t.getTaskId() == s.getId()).collect(Collectors.toList());
//                //获取当前关联模块id列表
//                List<Integer> currentModuleIds = currentTaskModuleEntities.stream().map(t->t.getModuleId()).collect(Collectors.toList());
//                //根据当前关联模块id列表获取模块详情
//                List<ModuleEntity> currentModuleEntities = moduleEntities.stream().filter(t->currentModuleIds.contains(t.getId())).collect(Collectors.toList());
//
//                //设置到当前任务Dto对象
//                List<ModuleOutputDto> currentModuleOutputDtos = currentModuleEntities.stream().map(t->modelMapper.map(t,ModuleOutputDto.class)).collect(Collectors.toList());
//                s.setModules(currentModuleOutputDtos);
//            });
//
//            responseData = ResponseData.success(outputDtos);
//            responseData.setTotal(queryPage.getTotal());
//        }catch (Exception ex){
//            log.error("操作异常：",ex);
//            responseData = ResponseData.failure("操作异常："+ex.toString());
//        }
//
//        return responseData;
//    }

    @Override
    public ResponseData<List<TaskOutputDto>> queryByProjectId(Integer projectId) {
        ResponseData<List<TaskOutputDto>> responseData;

        try {
            QueryWrapper<TaskEntity> queryWrapper = new QueryWrapper<>();
            if(projectId != null) {
                queryWrapper.eq("project_id", projectId);
            }
            queryWrapper.eq("is_delete",false); //只取没有删除的
            queryWrapper.orderByDesc("id");
            List <TaskEntity> entities = this.list(queryWrapper);
            List <TaskOutputDto> outputDtos = entities.stream().map(s -> modelMapper.map(s, TaskOutputDto.class)).collect(Collectors.toList());

            responseData = ResponseData.success(outputDtos);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<List<TaskDetailOutputDto>> queryDetailByProjectId(Integer projectId) {
        ResponseData<List<TaskDetailOutputDto>> responseData;

        if(projectId == null){
            responseData = new ResponseData <>();
            responseData.setCode(1);
            responseData.setMessage("项目ID不能为空");

            return responseData;
        }

        try {
            QueryWrapper<TaskEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("project_id", projectId);
            queryWrapper.eq("is_delete",false); //只取没有删除的
            queryWrapper.eq("is_archive",false); //只取没有归档的
            List <TaskEntity> entities = this.list(queryWrapper);
            List <TaskDetailOutputDto> outputDtos = entities.stream().map(s -> modelMapper.map(s, TaskDetailOutputDto.class)).collect(Collectors.toList());

            //获取任务所有的测试套件
            QueryWrapper<TestSuitEntity> testSuitEntityQueryWrapper = new QueryWrapper <>();
            testSuitEntityQueryWrapper.eq("project_id", projectId);
            testSuitEntityQueryWrapper.eq("is_delete",false); //只取没有删除的
            List <TestSuitEntity> testSuitEntities = testSuitService.list(testSuitEntityQueryWrapper);

            //获取项目所有的测试用例
            QueryWrapper<TestCaseEntity> testCaseEntityQueryWrapper = new QueryWrapper <>();
            testCaseEntityQueryWrapper.eq("project_id", projectId);
            testCaseEntityQueryWrapper.eq("is_delete",false); //只取没有删除的
            List <TestCaseEntity> testCaseEntities = testCaseService.list(testCaseEntityQueryWrapper);

            //将测试套件、测试用例归类到任务下去
            //遍历所有任务，获取任务下的所有测试套件
            outputDtos.stream().forEach(taskDetailOutputDto->{
                List<TestSuitEntity> currentTestSuitEntities = testSuitEntities.stream().filter(testSuitEntity->testSuitEntity.getTaskId() == taskDetailOutputDto.getId()).collect(Collectors.toList());
                List<TestSuitDetailOutputDto> testSuitDetailOutputDtos = currentTestSuitEntities.stream().map(testSuitEntity->modelMapper.map(testSuitEntity,TestSuitDetailOutputDto.class)).collect(Collectors.toList());
                //遍历当前所有测试套件，获取测试套件下的测试用例
                testSuitDetailOutputDtos.stream().forEach(testSuitDetailOutputDto->{
                    List<TestCaseEntity> currentTestCaseEntities = testCaseEntities.stream().filter(testCaseEntity->testCaseEntity.getTestSuitId() == testSuitDetailOutputDto.getId()).collect(Collectors.toList());
                    List<TestCaseOutputDto> testCaseDetailOutputDtos = currentTestCaseEntities.stream().map(testCaseEntity->modelMapper.map(testCaseEntity,TestCaseOutputDto.class)).collect(Collectors.toList());

                    testSuitDetailOutputDto.setTestCases(testCaseDetailOutputDtos);
                });

                taskDetailOutputDto.setTestSuits(testSuitDetailOutputDtos);
            });

            responseData = ResponseData.success(outputDtos);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }
//
//    @Override
//    public ResponseData <TaskOutputDto> getById(Integer id) {
//        ResponseData<TaskOutputDto> responseData;
//        try {
//            TaskEntity entity = super.getById(id);
//
//            //获取关联模块id
//            QueryWrapper<TaskModuleEntity> taskModuleEntityQueryWrapper = new QueryWrapper<>();
//            taskModuleEntityQueryWrapper.eq("task_id",id);
//            List<TaskModuleEntity> taskEntities = taskModuleService.list(taskModuleEntityQueryWrapper);
//            List<Integer> moduleIds = taskEntities.stream().map(s->s.getModuleId()).collect(Collectors.toList());
//
//            //Entity转DTO
//            TaskOutputDto outputDto = modelMapper.map(entity,TaskOutputDto.class);
//            outputDto.setModuleIds(moduleIds);
//
//            responseData = ResponseData.success(outputDto);
//        }catch (Exception ex){
//            log.error("操作异常：",ex);
//            responseData = ResponseData.failure("操作异常："+ex.toString());
//        }
//
//        return responseData;
//    }

//    @Override
//    @Transactional
//    public ResponseData<TaskOutputDto> create(TaskCreateInputDto inputDto) {
//        ResponseData<TaskOutputDto> responseData;
//
//        try {
//            //数据验证
//            List<String> checkMsgs = new ArrayList <>();
//            //当前项目模块名称是否存在
//            QueryWrapper<TaskEntity> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("name", inputDto.getName());
//            queryWrapper.eq("is_delete",false);
//            queryWrapper.eq("project_id",inputDto.getProjectId());
//            TaskEntity taskEntity = this.getOne(queryWrapper,false);
//            if (taskEntity!=null) {
//                checkMsgs.add("任务名称已经存在");
//            }
//            //所属项目是否存在
//            QueryWrapper<ProjectEntity> projectQueryWrapper = new QueryWrapper<>();
//            projectQueryWrapper.eq("id", inputDto.getProjectId());
//            projectQueryWrapper.eq("is_delete", false);
//            Integer existCount = projectService.count(projectQueryWrapper);
//            if(existCount<=0){
//                checkMsgs.add("所属项目不存在");
//            }
//            //关联模块
//            if(inputDto.getModuleIds()!=null && inputDto.getModuleIds().size()>0){
//                List<Integer> moduleIds = inputDto.getModuleIds();
//                //根据当前模块id，查询出所有存在的模块信息
//                QueryWrapper<ModuleEntity> moduleQueryWrapper = new QueryWrapper<>();
//                moduleQueryWrapper.in("id", moduleIds);
//                moduleQueryWrapper.eq("is_delete", false);
//                List<ModuleEntity> existsModules = moduleService.list(moduleQueryWrapper);
//                //如果查询出来的模块信息与不包含当前传入的模块信息，则表示，传入的部分id不存在
//                List<Integer> nonExistsIds = moduleIds.stream().filter(s->existsModules.stream().anyMatch(t->t.getId() == s) == false).collect(Collectors.toList());
//                if(nonExistsIds.size()>0){
//                    checkMsgs.add("关联模块["+  nonExistsIds.stream().map(s->s.toString()).collect(Collectors.joining(",")) +"]不存在");
//                }
//            }
//            if(checkMsgs.size()>0){
//                responseData = new ResponseData <>();
//                responseData.setCode(1);
//                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));
//
//                return responseData;
//            }
//
//            //DTO转Entity
//            TaskEntity entity = modelMapper.map(inputDto,TaskEntity.class);
//            //设置默认值
//            entity.setIsDelete(false);
//            entity.setIsArchive(false);
//            //新增任务
//            this.save(entity);
//            //保存关联模块
//            if(inputDto.getModuleIds()!=null && inputDto.getModuleIds().size()>0){
//                for(Integer moduleId: inputDto.getModuleIds()){
//                    TaskModuleEntity taskModuleEntity = new TaskModuleEntity();
//                    taskModuleEntity.setTaskId(entity.getId());
//                    taskModuleEntity.setModuleId(moduleId);
//
//                    taskModuleService.save(taskModuleEntity);
//                }
//            }
//            //Entity转DTO
//            TaskOutputDto outputDto = modelMapper.map(entity,TaskOutputDto.class);
//
//            responseData = ResponseData.success(outputDto);
//        }catch (Exception ex){
//            log.error("操作异常：",ex);
//            responseData = ResponseData.failure("操作异常："+ex.toString());
//        }
//
//        return responseData;
//    }
//
//    @Override
//    @Transactional
//    public ResponseData<TaskOutputDto> update(TaskUpdateInputDto inputDto) {
//        ResponseData<TaskOutputDto> responseData;
//
//        try {
//            //数据验证
//            List<String> checkMsgs = new ArrayList <>();
//            //模块名称是否存在
//            QueryWrapper<TaskEntity> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("name", inputDto.getName());
//            queryWrapper.eq("is_delete",false);
//            queryWrapper.eq("project_id",inputDto.getProjectId());
//            queryWrapper.ne("id",inputDto.getId());
//            TaskEntity taskEntity = this.getOne(queryWrapper,false);
//            if (taskEntity!=null) {
//                checkMsgs.add("任务名称已经存在");
//            }
//            //所属项目是否存在
//            QueryWrapper<ProjectEntity> projectQueryWrapper = new QueryWrapper<>();
//            projectQueryWrapper.eq("id", inputDto.getProjectId());
//            projectQueryWrapper.eq("is_delete", false);
//            Integer existCount = projectService.count(projectQueryWrapper);
//            if(existCount<=0){
//                checkMsgs.add("所属项目不存在");
//            }
//            //验证关联模块
//            List<TaskModuleEntity> existsTaskModuleEntities = null;
//            if(inputDto.getModuleIds()!=null && inputDto.getModuleIds().size()>0){
//                List<Integer> moduleIds = inputDto.getModuleIds();
//                //验证关联模块是否存在
//                //根据当前模块id，查询出所有存在的模块信息
//                QueryWrapper<ModuleEntity> moduleQueryWrapper = new QueryWrapper<>();
//                moduleQueryWrapper.in("id", moduleIds);
//                moduleQueryWrapper.eq("is_delete", false);
//                List<ModuleEntity> existsModules = moduleService.list(moduleQueryWrapper);
//                //如果查询出来的模块信息与不包含当前传入的模块信息，则表示，传入的部分id不存在
//                List<Integer> nonExistsIds = moduleIds.stream().filter(s->existsModules.stream().anyMatch(t->t.getId() == s) == false).collect(Collectors.toList());
//                if(nonExistsIds.size()>0){
//                    checkMsgs.add("关联模块["+  nonExistsIds.stream().map(s->s.toString()).collect(Collectors.joining(",")) +"]不存在");
//                }
//                //验证关联模块相比旧数据，是否有删除；需要保证修改时，只能添加模块，不能删除模块，因为模块下，可能已经添加了用例
//                QueryWrapper<TaskModuleEntity> taskModuleEntityQueryWrapper = new QueryWrapper<>();
//                taskModuleEntityQueryWrapper.eq("task_id",inputDto.getId());
//                existsTaskModuleEntities = taskModuleService.list(taskModuleEntityQueryWrapper);
//                if(existsTaskModuleEntities!=null && existsTaskModuleEntities.size()>0){
//                    if(existsTaskModuleEntities.stream().filter(s->inputDto.getModuleIds().contains(s.getModuleId()) == false).count()>0){
//                        checkMsgs.add("修改任务时，只能新增模块，不能删除；因为任务已关联模块可能已经在测试套件中添加接口和测试用例");
//                    }
//                }
//            }
//            if(checkMsgs.size()>0){
//                responseData = new ResponseData <>();
//                responseData.setCode(1);
//                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));
//
//                return responseData;
//            }
//
//            //DTO转Entity
//            TaskEntity entity = modelMapper.map(inputDto,TaskEntity.class);
//            //设置默认值
//            entity.setIsDelete(false);
//            //更新
//            this.updateById(entity);
//            //对于新增的模块，添加到任务模块关联数据
//
//            if(inputDto.getModuleIds()!=null && inputDto.getModuleIds().size()>0){
//                //获取新增的模块id
//                List<TaskModuleEntity> finalExistsTaskModuleEntities = existsTaskModuleEntities;
//                List<Integer> newMoudelIds = inputDto.getModuleIds().stream().filter(s-> finalExistsTaskModuleEntities.stream().filter(t->t.getModuleId() == s).count()<=0).collect(Collectors.toList());
//                List<TaskModuleEntity> newTaskModuleEntities = new ArrayList<>();
//                for(Integer moduleId: newMoudelIds){
//                    TaskModuleEntity taskModuleEntity = new TaskModuleEntity();
//                    taskModuleEntity.setTaskId(entity.getId());
//                    taskModuleEntity.setModuleId(moduleId);
//
//                    newTaskModuleEntities.add(taskModuleEntity);
//                }
//                if(newTaskModuleEntities.size()>0){
//                    taskModuleService.saveBatch(newTaskModuleEntities);
//                }
//            }
//            //Entity转DTO
//            TaskOutputDto outputDto = modelMapper.map(entity,TaskOutputDto.class);
//
//            responseData = ResponseData.success(outputDto);
//        }catch (Exception ex){
//            log.error("操作异常：",ex);
//            responseData = ResponseData.failure("操作异常："+ex.toString());
//        }
//
//        return responseData;
//    }
//
//    @Override
//    public ResponseData<Boolean> delete(Integer id) {
//        ResponseData<Boolean> responseData;
//
//        try {
//            //数据验证
//            List<String> checkMsgs = new ArrayList <>();
//            //项目名称是否存在
//            QueryWrapper<TaskEntity> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("id",id);
//            queryWrapper.eq("is_delete",false);
//            TaskEntity taskEntity = this.getOne(queryWrapper,false);
//            if (taskEntity!=null) {
//                //如果任务下有测试记录，则不能删除
//                QueryWrapper<TestRecordEntity> testRecordEntityQueryWrapper = new QueryWrapper<>();
//                if(id != null) {
//                    testRecordEntityQueryWrapper.eq("task_id", id);
//                }
//                testRecordEntityQueryWrapper.eq("is_delete",false);
//                List <TestRecordEntity> entities = testRecordService.list(testRecordEntityQueryWrapper);
//                if(entities!=null && entities.size()>0){
//                    checkMsgs.add("任务已有运行记录，不能删除");
//                }
//            }else{
//                checkMsgs.add("任务不存在");
//            }
//
//            if(checkMsgs.size()>0){
//                responseData = new ResponseData <>();
//                responseData.setCode(1);
//                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));
//
//                return responseData;
//            }
//
//            //修改状态
//            taskEntity.setIsDelete(true);
//            Boolean result = this.updateById(taskEntity);
//
//            responseData = ResponseData.success(result);
//        }catch (Exception ex){
//            log.error("操作异常：",ex);
//            responseData = ResponseData.failure("操作异常："+ex.toString());
//        }
//
//        return responseData;
//    }
//
    @Override
    public ResponseData<List<ModuleOutputDto>> getModulesByTaskId(Integer taskId) {
        ResponseData<List<ModuleOutputDto>> responseData;

        try {
            QueryWrapper<TaskModuleEntity> taskModuleEntityQueryWrapper = new QueryWrapper<>();
            if(taskId != null) {
                taskModuleEntityQueryWrapper.eq("task_id", taskId);
            }
            List <TaskModuleEntity> taskModuleEntities = taskModuleService.list(taskModuleEntityQueryWrapper);
            List<Integer> modudleIds = taskModuleEntities.stream().map(s->s.getModuleId()).collect(Collectors.toList());

            QueryWrapper<ModuleEntity> moduleEntityQueryWrapper = new QueryWrapper<>();
            moduleEntityQueryWrapper.in("id",modudleIds);
            moduleEntityQueryWrapper.eq("is_delete",false);
            List<ModuleEntity> moduleEntities = moduleService.list(moduleEntityQueryWrapper);

            List <ModuleOutputDto> outputDtos = moduleEntities.stream().map(s -> modelMapper.map(s, ModuleOutputDto.class)).collect(Collectors.toList());

            responseData = ResponseData.success(outputDtos);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

//    /**
//     * 运行任务实现，首先快速添加添加测试记录后返回，然后开启线程运行测试任务
//     * 1、添加测试记录
//     * 2、获相关数据，包括项目环境、接口列表、接口对应测试用例列表、测试记录
//     * @param taskRunInputDto
//     * @return
//     */
//    public ResponseData<Boolean> run(TaskRunInputDto taskRunInputDto){
//        ResponseData<Boolean> responseData;
//
//        //1、数据验证&获取基础数据
//        List<String> checkMsgs = new ArrayList <>();
//        //项目环境
//        EnvironmentEntity environmentEntity = null;
//        //测试任务关联模块下的接口列表
//        List<InterfaceEntity> interfaceEntities = null;
//        //测试任务下的关联测试套件
//        List<TestSuitEntity> testSuitEntities = null;
//        //测试任务下关联测试套件下的测试用例
//        List<TestCaseEntity> testCaseEntities = null;
//        try{
//            //获取任务
//            TaskEntity taskEntity = this.getById((Serializable)taskRunInputDto.getTaskId());
//            if(taskEntity!=null) {
//                //获取项目环境
//                environmentEntity = environmentService.getById((Serializable) taskRunInputDto.getEnvironmentId());
//                if(environmentEntity == null){
//                    checkMsgs.add("项目环境不存在");
//                }
//                //根据测试任务获取关联模块
//                QueryWrapper<TaskModuleEntity> queryWrapperOfModules = new QueryWrapper<>();
//                queryWrapperOfModules.eq("task_id", taskRunInputDto.getTaskId());
//                List<TaskModuleEntity> taskModuleEntities = taskModuleService.list(queryWrapperOfModules);
//                //根据模块获取所有接口
//                if (taskModuleEntities != null && taskModuleEntities.size() > 0) {
//                    QueryWrapper<InterfaceEntity> queryWrapperOfInterfaces = new QueryWrapper<>();
//                    queryWrapperOfInterfaces.in("module_id", taskModuleEntities.stream().map(s -> s.getModuleId()).collect(Collectors.toList()));
//                    queryWrapperOfInterfaces.eq("is_delete", false);
//                    interfaceEntities = interfaceService.list(queryWrapperOfInterfaces);
//                    if (interfaceEntities == null || interfaceEntities.size() <= 0) {
//                        checkMsgs.add(String.format("任务[%s]关联模块下没有接口", taskEntity.getName()));
//                    }
//                } else {
//                    checkMsgs.add(String.format("任务[%s]没有关联模块", taskEntity.getName()));
//                }
//
//                //根据测试任务获取所有套件
//                QueryWrapper<TestSuitEntity> testSuitEntityQueryWrapper = new QueryWrapper<>();
//                testSuitEntityQueryWrapper.eq("task_id", taskRunInputDto.getTaskId());
//                testSuitEntityQueryWrapper.eq("is_delete", false);
//                testSuitEntities = testSuitService.list(testSuitEntityQueryWrapper);
//                if (testSuitEntities != null && testSuitEntities.size() > 0) {
//                    //根据测试套件获取所有测试用例列表
//                    QueryWrapper<TestCaseEntity> queryWrapperOfTestCases = new QueryWrapper<>();
//                    queryWrapperOfTestCases.in("task_id", taskRunInputDto.getTaskId());
//                    queryWrapperOfTestCases.in("test_suit_id", testSuitEntities.stream().map(s -> s.getId()).collect(Collectors.toList()));
//                    queryWrapperOfTestCases.eq("is_delete", false);
//                    testCaseEntities = testCaseService.list(queryWrapperOfTestCases);
//                    if (testCaseEntities == null || testCaseEntities.size() <= 0) {
//                        checkMsgs.add(String.format("任务[%s]下没有测试用例", taskEntity.getName()));
//                    }
//                } else {
//                    checkMsgs.add(String.format("任务[%s]下没有测试套件", taskEntity.getName()));
//                }
//            }else{
//                checkMsgs.add("任务不存在");
//            }
//        }catch (Exception ex){
//            checkMsgs.add("获取任务基础数据异常");
//        }
//        if(checkMsgs.size()>0){
//            responseData = new ResponseData <>();
//            responseData.setCode(1);
//            responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));
//
//            return responseData;
//        }
//
//        //2、运行测试任务前，添加一条测试记录，状态为执行中
//        TestRecordEntity testRecordEntity =  modelMapper.map(taskRunInputDto,TestRecordEntity.class);
//        testRecordEntity.setStatus(1);
//        testRecordEntity.setIsDelete(false);
//        //由于拦截添加创建和修改信息无法在多线程共享会话，需要手动维护创建和修改信息
//        Date current = new Date();
//        UserEntity userEntity = sessionUtil.getCurrentUser().getUserEntity();
//        testRecordEntity.setCreateById(userEntity.getId());
//        testRecordEntity.setCreateByName(userEntity.getName());
//        testRecordEntity.setCreateTime(current);
//        testRecordEntity.setUpdateById(userEntity.getId());
//        testRecordEntity.setUpdateByName(userEntity.getName());
//        testRecordEntity.setUpdateTime(current);
//        testRecordService.save(testRecordEntity);
//
//        //3、运行测试任务，通过添加一个新线程执行测试任务，当前主线程快速返回给调用用户，如果测试用例数量大于0，则启动子线程执行测试任务
//        List <InterfaceEntity> finalInterfaceEntities = interfaceEntities;
//        List <TestCaseEntity> finalTestCaseEntities = testCaseEntities;
//        EnvironmentEntity finalEnvironmentEntity = environmentEntity;
//        SessionUtil.CurrentUser currentUser =sessionUtil.getCurrentUser();
//        new Thread(new Runnable() {
//            //转为运行时异常
//            @SneakyThrows
//            @Override
//            public void run() {
//                //测试
//                taskTestService.test(finalEnvironmentEntity, finalInterfaceEntities, finalTestCaseEntities,testRecordEntity,currentUser);
//            }
//        }).start();
//
//        responseData = ResponseData.success();
//        return responseData;
//    }
//
//    @Override
//    public ResponseData<Boolean> archive(Integer id) {
//        ResponseData<Boolean> responseData;
//
//        try {
//            //数据验证
//            List<String> checkMsgs = new ArrayList<>();
//            //项目名称是否存在
//            QueryWrapper<TaskEntity> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("id",id);
//            queryWrapper.eq("is_delete",false);
//            TaskEntity taskEntity = this.getOne(queryWrapper,false);
//            if (taskEntity==null) {
//                checkMsgs.add("任务不存在");
//            }
//            if(checkMsgs.size()>0){
//                responseData = new ResponseData<>();
//                responseData.setCode(1);
//                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));
//
//                return responseData;
//            }
//
//            //修改状态
//            taskEntity.setIsArchive(true);
//            Boolean result = this.updateById(taskEntity);
//
//            responseData = ResponseData.success(result);
//        }catch (Exception ex){
//            log.error("操作异常：",ex);
//            responseData = ResponseData.failure("操作异常："+ex.toString());
//        }
//
//        return responseData;
//    }
}
