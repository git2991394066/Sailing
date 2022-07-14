package com.sailing.interfacetestplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.testreport.TestReportCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.testreport.TestReportUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.testreport.TestReportOutputDto;
import com.sailing.interfacetestplatform.entity.ProjectEntity;
import com.sailing.interfacetestplatform.entity.TestRecordEntity;
import com.sailing.interfacetestplatform.entity.TestReportEntity;
import com.sailing.interfacetestplatform.mapper.TestReportMapper;
import com.sailing.interfacetestplatform.service.ProjectService;
import com.sailing.interfacetestplatform.service.TestRecordService;
import com.sailing.interfacetestplatform.service.TestReportService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther:张启航Sailling
 * @createDate:2022/7/14/0014 11:30:42
 * @description:测试报告服务实现
 **/
@Service
public class TestReportServiceImpl extends ServiceImpl<TestReportMapper, TestReportEntity> implements TestReportService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    @Lazy
    TestRecordService testRecordService;
    @Autowired
    ProjectService projectService;

    @Override
    public ResponseData<List<TestReportOutputDto>> query(Integer pageIndex, Integer pageSize, Integer testRecordId, Integer projectId) {
        ResponseData<List<TestReportOutputDto>> responseData;

        try {
            QueryWrapper<TestReportEntity> queryWrapper = new QueryWrapper<>();
            if(testRecordId != null) {
                queryWrapper.eq("test_record_id", testRecordId);
            }
            if(projectId != null) {
                queryWrapper.eq("project_id", projectId);
            }
            queryWrapper.eq("is_delete",false); //只取没有删除的
            queryWrapper.orderByDesc("id");
            IPage<TestReportEntity> queryPage = new Page<>(pageIndex, pageSize);
            queryPage = this.page(queryPage,queryWrapper);

            responseData = ResponseData.success(queryPage.getRecords().stream().map(s->modelMapper.map(s, TestReportOutputDto.class)));
            responseData.setTotal(queryPage.getTotal());
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<List<TestReportOutputDto>> queryByProjectId(Integer projectId) {
        ResponseData<List<TestReportOutputDto>> responseData;

        try {
            QueryWrapper<TestReportEntity> queryWrapper = new QueryWrapper<>();
            if(projectId != null) {
                queryWrapper.eq("project_id", projectId);
            }
            queryWrapper.eq("is_delete",false); //只取没有删除的
            queryWrapper.orderByDesc("id");
            List<TestReportEntity> entities = this.list(queryWrapper);
            List <TestReportOutputDto> outputDtos = entities.stream().map(s -> modelMapper.map(s, TestReportOutputDto.class)).collect(Collectors.toList());

            responseData = ResponseData.success(outputDtos);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData <TestReportOutputDto> getById(Integer id) {
        ResponseData<TestReportOutputDto> responseData;
        try {
            TestReportEntity entity = super.getById(id);

            //Entity转DTO
            TestReportOutputDto outputDto = modelMapper.map(entity,TestReportOutputDto.class);

            responseData = ResponseData.success(outputDto);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<TestReportOutputDto> create(TestReportCreateInputDto inputDto) {
        ResponseData<TestReportOutputDto> responseData;

        try {
            //数据验证
            List<String> checkMsgs = new ArrayList<>();
            //所属测试记录是否存在
            QueryWrapper<TestRecordEntity> testRecordEntityQueryWrapper = new QueryWrapper<>();
            testRecordEntityQueryWrapper.eq("id", inputDto.getTestRecordId());
            testRecordEntityQueryWrapper.eq("is_delete", false);
            Integer existCount = testRecordService.count(testRecordEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属运行记录不存在");
            }
            //所属项目是否存在
            QueryWrapper<ProjectEntity> projectQueryWrapper = new QueryWrapper<>();
            projectQueryWrapper.eq("id", inputDto.getProjectId());
            projectQueryWrapper.eq("is_delete", false);
            existCount = projectService.count(projectQueryWrapper);
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
            TestReportEntity entity = modelMapper.map(inputDto,TestReportEntity.class);
            //设置默认值
            entity.setIsDelete(false);
            //新增
            this.save(entity);
            //Entity转DTO
            TestReportOutputDto outputDto = modelMapper.map(entity,TestReportOutputDto.class);

            responseData = ResponseData.success(outputDto);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<TestReportOutputDto> update(TestReportUpdateInputDto inputDto) {
        ResponseData<TestReportOutputDto> responseData;

        try {
            //数据验证
            List<String> checkMsgs = new ArrayList <>();
            //所属测试记录是否存在
            QueryWrapper<TestRecordEntity> testRecordEntityQueryWrapper = new QueryWrapper<>();
            testRecordEntityQueryWrapper.eq("id", inputDto.getTestRecordId());
            testRecordEntityQueryWrapper.eq("is_delete", false);
            Integer existCount = testRecordService.count(testRecordEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属运行记录不存在");
            }
            //所属项目是否存在
            QueryWrapper<ProjectEntity> projectQueryWrapper = new QueryWrapper<>();
            projectQueryWrapper.eq("id", inputDto.getProjectId());
            projectQueryWrapper.eq("is_delete", false);
            existCount = projectService.count(projectQueryWrapper);
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
            TestReportEntity entity = modelMapper.map(inputDto,TestReportEntity.class);
            //设置默认值
            entity.setIsDelete(false);
            //新增
            this.save(entity);
            //Entity转DTO
            TestReportOutputDto outputDto = modelMapper.map(entity,TestReportOutputDto.class);

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
            QueryWrapper<TestReportEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",id);
            queryWrapper.eq("is_delete",false);
            TestReportEntity moduleEntity = this.getOne(queryWrapper,false);
            if (moduleEntity==null) {
                checkMsgs.add("测试报告不存在");
            }
            if(checkMsgs.size()>0){
                responseData = new ResponseData<>();
                responseData.setCode(1);
                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));

                return responseData;
            }

            //修改状态
            moduleEntity.setIsDelete(true);
            Boolean result = this.updateById(moduleEntity);

            responseData = ResponseData.success(result);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }
}

