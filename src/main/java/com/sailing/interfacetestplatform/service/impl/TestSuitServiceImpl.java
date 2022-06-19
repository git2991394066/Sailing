package com.sailing.interfacetestplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.testsuit.TestSuitCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.testsuit.TestSuitUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.testsuit.TestSuitOutputDto;
import com.sailing.interfacetestplatform.entity.TaskEntity;
import com.sailing.interfacetestplatform.entity.TestSuitEntity;
import com.sailing.interfacetestplatform.mapper.TestSuitMapper;
import com.sailing.interfacetestplatform.service.TaskService;
import com.sailing.interfacetestplatform.service.TestSuitService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 0:52:48
 * @description:测试套件服务实现
 **/
@Service
public class TestSuitServiceImpl extends ServiceImpl<TestSuitMapper, TestSuitEntity> implements TestSuitService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    TaskService taskService;

    @Override
    public ResponseData<List<TestSuitOutputDto>> queryByProjectId(Integer projectId) {
        ResponseData<List<TestSuitOutputDto>> responseData;

        try {
            QueryWrapper<TestSuitEntity> queryWrapper = new QueryWrapper<>();
            if(projectId != null) {
                queryWrapper.eq("project_id", projectId);
            }
            queryWrapper.eq("is_delete",false); //只取没有删除的
            queryWrapper.orderByDesc("id");
            List <TestSuitEntity> entities = this.list(queryWrapper);
            List <TestSuitOutputDto> outputDtos = entities.stream().map(s -> modelMapper.map(s, TestSuitOutputDto.class)).collect(Collectors.toList());

            responseData = ResponseData.success(outputDtos);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData <TestSuitOutputDto> getById(Integer id) {
        ResponseData<TestSuitOutputDto> responseData;
        try {
            TestSuitEntity entity = super.getById(id);

            //Entity转DTO
            TestSuitOutputDto outputDto = modelMapper.map(entity,TestSuitOutputDto.class);

            responseData = ResponseData.success(outputDto);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<TestSuitOutputDto> create(TestSuitCreateInputDto inputDto) {
        ResponseData<TestSuitOutputDto> responseData;

        try {
            //数据验证
            List<String> checkMsgs = new ArrayList<>();
            //接口名称是否存在
            QueryWrapper<TestSuitEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", inputDto.getName());
            queryWrapper.eq("is_delete",false);
            queryWrapper.eq("project_id",inputDto.getProjectId());
            queryWrapper.eq("task_id",inputDto.getTaskId());
            TestSuitEntity testSuitEntity = this.getOne(queryWrapper,false);
            if (testSuitEntity!=null) {
                checkMsgs.add("测试套件名称已经存在");
            }
            //所属任务是否存在
            QueryWrapper<TaskEntity> taskEntityQueryWrapper = new QueryWrapper<>();
            taskEntityQueryWrapper.eq("id", inputDto.getTaskId());
            taskEntityQueryWrapper.eq("is_delete", false);
            Integer existCount = taskService.count(taskEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属任务不存在");
            }
            if(checkMsgs.size()>0){
                responseData = new ResponseData <>();
                responseData.setCode(1);
                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));

                return responseData;
            }

            //DTO转Entity
            TestSuitEntity entity = modelMapper.map(inputDto,TestSuitEntity.class);
            //设置默认值
            entity.setIsDelete(false);
            //新增
            this.save(entity);
            //Entity转DTO
            TestSuitOutputDto outputDto = modelMapper.map(entity,TestSuitOutputDto.class);

            responseData = ResponseData.success(outputDto);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<TestSuitOutputDto> update(TestSuitUpdateInputDto inputDto) {
        ResponseData<TestSuitOutputDto> responseData;

        try {
            //数据验证
            List<String> checkMsgs = new ArrayList <>();
            //接口名称是否存在
            QueryWrapper<TestSuitEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", inputDto.getName());
            queryWrapper.eq("is_delete",false);
            queryWrapper.eq("project_id",inputDto.getProjectId());
            queryWrapper.eq("task_id",inputDto.getTaskId());
            queryWrapper.ne("id",inputDto.getId());
            TestSuitEntity testSuitEntity = this.getOne(queryWrapper,false);
            if (testSuitEntity!=null) {
                checkMsgs.add("测试套件名称已经存在");
            }
            //所属任务是否存在
            QueryWrapper<TaskEntity> taskEntityQueryWrapper = new QueryWrapper<>();
            taskEntityQueryWrapper.eq("id", inputDto.getTaskId());
            taskEntityQueryWrapper.eq("is_delete", false);
            Integer existCount = taskService.count(taskEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属任务不存在");
            }
            if(checkMsgs.size()>0){
                responseData = new ResponseData <>();
                responseData.setCode(1);
                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));

                return responseData;
            }

            //DTO转Entity
            TestSuitEntity entity = modelMapper.map(inputDto,TestSuitEntity.class);
            //设置默认值
            entity.setIsDelete(false);
            //新增
            this.updateById(entity);
            //Entity转DTO
            TestSuitOutputDto outputDto = modelMapper.map(entity,TestSuitOutputDto.class);

            responseData = ResponseData.success(outputDto);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<Boolean> delete(Integer id) {
        ResponseData<Boolean> responseData;

        try {
            //数据验证
            List<String> checkMsgs = new ArrayList <>();
            //项目名称是否存在
            QueryWrapper<TestSuitEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",id);
            queryWrapper.eq("is_delete",false);
            TestSuitEntity testSuitEntity = this.getOne(queryWrapper,false);
            if (testSuitEntity==null) {
                checkMsgs.add("测试套件不存在");
            }
            if(checkMsgs.size()>0){
                responseData = new ResponseData <>();
                responseData.setCode(1);
                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));

                return responseData;
            }

            //修改状态
            testSuitEntity.setIsDelete(true);
            Boolean result = this.updateById(testSuitEntity);

            responseData = ResponseData.success(result);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }
}
