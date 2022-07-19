package com.sailing.interfacetestplatform.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.testrecord.TestRecordCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.testrecord.TestRecordUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.task.TaskOutputDto;
import com.sailing.interfacetestplatform.dto.output.testrecord.TestRecordOutputDto;
import com.sailing.interfacetestplatform.dto.output.testrecord.TestRecordQueryOutputDto;
import com.sailing.interfacetestplatform.dto.output.testreport.TestReportOutputDto;
import com.sailing.interfacetestplatform.dto.output.testreport.TestResultOutputDto;
import com.sailing.interfacetestplatform.entity.*;
import com.sailing.interfacetestplatform.mapper.TestRecordMapper;
import com.sailing.interfacetestplatform.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther:张启航Sailling
 * @createDate:2022/7/14/0014 11:16:14
 * @description:测试记录服务实现
 **/
@Service
public class TestRecordServiceImpl extends ServiceImpl<TestRecordMapper, TestRecordEntity> implements TestRecordService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    EnvironmentService environmentService;
    @Autowired
    @Lazy
    TaskService taskService;
    @Autowired
    ProjectService projectService;
    @Autowired
    @Lazy
    TestReportService testReportService;

    @Override
    public ResponseData<List<TestRecordQueryOutputDto>> query(Integer pageIndex, Integer pageSize, Integer environmentId, Integer taskId, Integer projectId) {
        ResponseData<List<TestRecordQueryOutputDto>> responseData;

        try {
            QueryWrapper<TestRecordEntity> queryWrapper = new QueryWrapper<>();
            if(environmentId != null) {
                queryWrapper.eq("environment_id", environmentId);
            }
            if(taskId != null) {
                queryWrapper.eq("task_id", taskId);
            }
            if(projectId != null) {
                queryWrapper.eq("project_id", projectId);
            }
            queryWrapper.eq("is_delete",false); //只取没有删除的
            queryWrapper.orderByDesc("id");
            IPage<TestRecordEntity> queryPage = new Page<>(pageIndex, pageSize);
            queryPage = this.page(queryPage,queryWrapper);
            List<TestRecordQueryOutputDto> testRecordQueryOutputDtos = queryPage.getRecords().stream().map(s->modelMapper.map(s, TestRecordQueryOutputDto.class)).collect(Collectors.toList());

            //获取所属任务
            List<Integer> taskIds = testRecordQueryOutputDtos.stream().map(s->s.getTaskId()).collect(Collectors.toList());
            QueryWrapper<TaskEntity> taskOutputDtoQueryWrapper = new QueryWrapper <>();
            taskOutputDtoQueryWrapper.in("id",taskIds);
            List<TaskEntity> taskEntities = taskService.list(taskOutputDtoQueryWrapper);


            //获取关联报告
            List<Integer> testRecordIds = testRecordQueryOutputDtos.stream().map(s->s.getId()).collect(Collectors.toList());
            QueryWrapper<TestReportEntity> testReportEntityQueryWrapper = new QueryWrapper<>();
            testReportEntityQueryWrapper.in("test_record_id", testRecordIds);
            List<TestReportEntity> testReportEntities = testReportService.list(testReportEntityQueryWrapper);

            //设置关联数据
            testRecordQueryOutputDtos.stream().forEach(s->{
                //关联任务
                TaskEntity taskEntity = taskEntities.stream().filter(t->t.getId() == s.getTaskId()).findFirst().orElse(null);
                if(taskEntity!=null){
                    s.setTask(modelMapper.map(taskEntity, TaskOutputDto.class));
                }
                //关联报告
                TestReportEntity testReportEntity = testReportEntities.stream().filter(t->t.getTestRecordId().intValue() ==s.getId().intValue()).findFirst().orElse(null);
                if(testReportEntity!=null){
                    TestReportOutputDto testReportOutputDto = modelMapper.map(testReportEntity, TestReportOutputDto.class);
                    //由于测试报告的结果Result字段作为快照数据，存储内容较多，只取到需要的，返回时，置为空
                    TestResultOutputDto testResultOutputDto = modelMapper.map(JSON.parse(testReportOutputDto.getResult()),TestResultOutputDto.class);
                    testReportOutputDto.setTotalOfTestCase(testResultOutputDto.getTotalOfTestCase());
                    testReportOutputDto.setTotalOfTestCaseForSuccess(testResultOutputDto.getTotalOfTestCaseForSuccess());
                    testReportOutputDto.setTotalOfTestCaseForFailure(testResultOutputDto.getTotalOfTestCaseForFailure());
                    testReportOutputDto.setTotalOfTestCaseForError(testResultOutputDto.getTotalOfTestCaseForError());
                    //结果置空
                    testReportOutputDto.setResult(null);

                    s.setTestReport(testReportOutputDto);
                }
            });

            responseData = ResponseData.success(testRecordQueryOutputDtos);
            responseData.setTotal(queryPage.getTotal());
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<List<TestRecordQueryOutputDto>> queryByProjectId(Integer taskId,Integer projectId) {
        ResponseData<List<TestRecordQueryOutputDto>> responseData;

        try {
            QueryWrapper<TestRecordEntity> queryWrapper = new QueryWrapper<>();
            if(taskId != null) {
                queryWrapper.eq("task_id", taskId);
            }
            if(projectId != null) {
                queryWrapper.eq("project_id", projectId);
            }
            queryWrapper.eq("is_delete",false); //只取没有删除的
            queryWrapper.eq("status",0);    //只取执行完成的
            queryWrapper.orderByDesc("id");
            List <TestRecordEntity> entities = this.list(queryWrapper);
            List <TestRecordQueryOutputDto> outputDtos = entities.stream().map(s -> modelMapper.map(s, TestRecordQueryOutputDto.class)).collect(Collectors.toList());

            //获取关联模块
            List<TestReportEntity> testReportEntities = new ArrayList<>();
            List<Integer> testRecordIds = outputDtos.stream().map(s->s.getId()).collect(Collectors.toList());
            if(testRecordIds!=null && testRecordIds.size()>0) {
                QueryWrapper<TestReportEntity> testReportEntityQueryWrapper = new QueryWrapper<>();
                testReportEntityQueryWrapper.in("test_record_id", testRecordIds);
                testReportEntities = testReportService.list(testReportEntityQueryWrapper);
            }

            List<TestReportEntity> finalTestReportEntities = testReportEntities;
            outputDtos.stream().forEach(s->{
                TestReportEntity testReportEntity = finalTestReportEntities.stream().filter(t->t.getTestRecordId().intValue() ==s.getId().intValue()).findFirst().orElse(null);
                if(testReportEntity!=null){
                    TestReportOutputDto testReportOutputDto = modelMapper.map(testReportEntity, TestReportOutputDto.class);
                    //由于测试报告的结果Result字段作为快照数据，存储内容较多，只取到需要的，返回时，置为空
                    TestResultOutputDto testResultOutputDto = modelMapper.map(JSON.parse(testReportOutputDto.getResult()),TestResultOutputDto.class);
                    testReportOutputDto.setTotalOfTestCase(testResultOutputDto.getTotalOfTestCase());
                    testReportOutputDto.setTotalOfTestCaseForSuccess(testResultOutputDto.getTotalOfTestCaseForSuccess());
                    testReportOutputDto.setTotalOfTestCaseForFailure(testResultOutputDto.getTotalOfTestCaseForFailure());
                    testReportOutputDto.setTotalOfTestCaseForError(testResultOutputDto.getTotalOfTestCaseForError());
                    //结果置空
                    testReportOutputDto.setResult(null);
                    s.setTestReport(testReportOutputDto);
                }
            });

            responseData = ResponseData.success(outputDtos);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData <TestRecordOutputDto> getById(Integer id) {
        ResponseData<TestRecordOutputDto> responseData;
        try {
            //获取测试记录
            TestRecordEntity entity = super.getById(id);
            //获取关联测试报告
            QueryWrapper<TestReportEntity> testReportEntityQueryWrapper = new QueryWrapper<>();
            testReportEntityQueryWrapper.eq("test_record_id",entity.getId());
            TestReportEntity testReportEntity = testReportService.getOne(testReportEntityQueryWrapper,false);

            //Entity转DTO
            TestRecordOutputDto outputDto = modelMapper.map(entity,TestRecordOutputDto.class);
            if(testReportEntity!=null){
                TestReportOutputDto testReportOutputDto = modelMapper.map(testReportEntity,TestReportOutputDto.class);
                //组织关联汇总数据，保持与分页查询相同
                TestResultOutputDto testResultOutputDto = modelMapper.map(JSON.parse(testReportOutputDto.getResult()),TestResultOutputDto.class);
                testReportOutputDto.setTotalOfTestCase(testResultOutputDto.getTotalOfTestCase());
                testReportOutputDto.setTotalOfTestCaseForSuccess(testResultOutputDto.getTotalOfTestCaseForSuccess());
                testReportOutputDto.setTotalOfTestCaseForFailure(testResultOutputDto.getTotalOfTestCaseForFailure());
                testReportOutputDto.setTotalOfTestCaseForError(testResultOutputDto.getTotalOfTestCaseForError());
                outputDto.setTestReport(testReportOutputDto);
            }

            responseData = ResponseData.success(outputDto);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<TestRecordOutputDto> create(TestRecordCreateInputDto inputDto) {
        ResponseData<TestRecordOutputDto> responseData;

        try {
            //数据验证
            List<String> checkMsgs = new ArrayList <>();
            //所属项目环境是否存在
            QueryWrapper<EnvironmentEntity> environmentEntityQueryWrapper = new QueryWrapper<>();
            environmentEntityQueryWrapper.eq("id", inputDto.getEnvironmentId());
            environmentEntityQueryWrapper.eq("is_delete", false);
            Integer existCount = environmentService.count(environmentEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属项目环境不存在");
            }
tian            //所属任务是否存在
            QueryWrapper<TaskEntity> taskEntityQueryWrapper = new QueryWrapper<>();
            taskEntityQueryWrapper.eq("id", inputDto.getTaskId());
            taskEntityQueryWrapper.eq("is_delete", false);
            existCount = taskService.count(taskEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属任务不存在");
            }
            //所属项目是否存在
            QueryWrapper<ProjectEntity> projectEntityQueryWrapper = new QueryWrapper<>();
            projectEntityQueryWrapper.eq("id", inputDto.getEnvironmentId());
            projectEntityQueryWrapper.eq("is_delete", false);
            existCount = projectService.count(projectEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属项目不存在");
            }
            if(checkMsgs.size()>0){
                responseData = new ResponseData <>();
                responseData.setCode(1);
                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));

                return responseData;
            }

            //DTO转Entity
            TestRecordEntity entity = modelMapper.map(inputDto,TestRecordEntity.class);
            //设置默认值
            entity.setIsDelete(false);
            //新增
            this.save(entity);
            //Entity转DTO
            TestRecordOutputDto outputDto = modelMapper.map(entity,TestRecordOutputDto.class);

            responseData = ResponseData.success(outputDto);
        }catch (Exception ex){
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.toString());
//            log.error("操作异常：",ex);
//            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<TestRecordOutputDto> update(TestRecordUpdateInputDto inputDto) {
        ResponseData<TestRecordOutputDto> responseData;

        try {
            //数据验证
            List<String> checkMsgs = new ArrayList <>();
            //所属项目环境是否存在
            QueryWrapper<EnvironmentEntity> environmentEntityQueryWrapper = new QueryWrapper<>();
            environmentEntityQueryWrapper.eq("id", inputDto.getEnvironmentId());
            environmentEntityQueryWrapper.eq("is_delete", false);
            Integer existCount = environmentService.count(environmentEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属项目环境不存在");
            }
            //所属项目环境是否存在
            QueryWrapper<TaskEntity> taskEntityQueryWrapper = new QueryWrapper<>();
            taskEntityQueryWrapper.eq("id", inputDto.getTaskId());
            taskEntityQueryWrapper.eq("is_delete", false);
            existCount = taskService.count(taskEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属任务不存在");
            }
            //所属项目是否存在
            QueryWrapper<ProjectEntity> projectEntityQueryWrapper = new QueryWrapper<>();
            projectEntityQueryWrapper.eq("id", inputDto.getEnvironmentId());
            projectEntityQueryWrapper.eq("is_delete", false);
            existCount = projectService.count(projectEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属项目不存在");
            }
            if(checkMsgs.size()>0){
                responseData = new ResponseData <>();
                responseData.setCode(1);
                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));

                return responseData;
            }

            //DTO转Entity
            TestRecordEntity entity = modelMapper.map(inputDto,TestRecordEntity.class);
            //设置默认值
            entity.setIsDelete(false);
            //新增
            this.updateById(entity);
            //Entity转DTO
            TestRecordOutputDto outputDto = modelMapper.map(entity,TestRecordOutputDto.class);

            responseData = ResponseData.success(outputDto);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }
}

