package com.sailing.interfacetestplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.testcase.TestCaseCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.testcase.TestCaseUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.testcase.TestCaseOutputDto;
import com.sailing.interfacetestplatform.entity.InterfaceEntity;
import com.sailing.interfacetestplatform.entity.TestCaseEntity;
import com.sailing.interfacetestplatform.entity.TestSuitEntity;
import com.sailing.interfacetestplatform.mapper.TestCaseMapper;
import com.sailing.interfacetestplatform.service.InterfaceService;
import com.sailing.interfacetestplatform.service.TestCaseService;
import com.sailing.interfacetestplatform.service.TestSuitService;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 0:44:01
 * @description:测试用例服务实现
 **/
@Service
public class TestCaseServiceImpl extends ServiceImpl<TestCaseMapper, TestCaseEntity> implements TestCaseService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    InterfaceService interfaceService;
    @Autowired
    @Lazy
    TestSuitService testSuitService;

    @Override
    public ResponseData<List<TestCaseOutputDto>> query(Integer pageIndex, Integer pageSize, Integer interfaceId, Integer testSuitId, Integer taskId, Integer projectId) {
        ResponseData<List<TestCaseOutputDto>> responseData;

        try {
            QueryWrapper<TestCaseEntity> queryWrapper = new QueryWrapper<>();
            if(interfaceId != null) {
                queryWrapper.eq("interface_id", interfaceId);
            }
            if(testSuitId != null) {
                queryWrapper.eq("test_suit_id", testSuitId);
            }
            if(taskId != null) {
                queryWrapper.eq("task_id", taskId);
            }
            if(projectId != null) {
                queryWrapper.eq("project_id", projectId);
            }
            queryWrapper.eq("is_delete",false); //只取没有删除的
            queryWrapper.orderByDesc("id");
            IPage<TestCaseEntity> queryPage = new Page<>(pageIndex, pageSize);
            queryPage = this.page(queryPage,queryWrapper);
            List<TestCaseOutputDto> interfaceQueryOutputDtos = queryPage.getRecords().stream().map(s->modelMapper.map(s, TestCaseOutputDto.class)).collect(Collectors.toList());


            responseData = ResponseData.success(interfaceQueryOutputDtos);
            responseData.setTotal(queryPage.getTotal());
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<List<TestCaseOutputDto>> queryByProjectId(Integer projectId) {
        ResponseData<List<TestCaseOutputDto>> responseData;

        try {
            QueryWrapper<TestCaseEntity> queryWrapper = new QueryWrapper<>();
            if(projectId != null) {
                queryWrapper.eq("project_id", projectId);
            }
            queryWrapper.eq("is_delete",false); //只取没有删除的
            queryWrapper.orderByDesc("id");
            List <TestCaseEntity> entities = this.list(queryWrapper);
            List <TestCaseOutputDto> outputDtos = entities.stream().map(s -> modelMapper.map(s, TestCaseOutputDto.class)).collect(Collectors.toList());

            responseData = ResponseData.success(outputDtos);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData <TestCaseOutputDto> getById(Integer id) {
        ResponseData<TestCaseOutputDto> responseData;
        try {
            TestCaseEntity entity = super.getById(id);

            //Entity转DTO
            TestCaseOutputDto outputDto = modelMapper.map(entity,TestCaseOutputDto.class);

            responseData = ResponseData.success(outputDto);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<TestCaseOutputDto> create(TestCaseCreateInputDto inputDto) {
        ResponseData<TestCaseOutputDto> responseData;

        try {
            //数据验证
            List<String> checkMsgs = new ArrayList<>();
            //测试用例名称是否存在
            QueryWrapper<TestCaseEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", inputDto.getName());
            queryWrapper.eq("is_delete",false);
            queryWrapper.eq("interface_id",inputDto.getInterfaceId());
            queryWrapper.eq("test_suit_id",inputDto.getTestSuitId());
            TestCaseEntity testCaseEntity = this.getOne(queryWrapper,false);
            if (testCaseEntity!=null) {
                checkMsgs.add("测试用例名称已经存在");
            }
            //所属接口是否存在
            QueryWrapper<InterfaceEntity> interfaceEntityQueryWrapper = new QueryWrapper<>();
            interfaceEntityQueryWrapper.eq("id", inputDto.getInterfaceId());
            interfaceEntityQueryWrapper.eq("is_delete", false);
            Integer existCount = interfaceService.count(interfaceEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属接口不存在");
            }
            //所属测试套件是否存在
            QueryWrapper<TestSuitEntity> testSuitEntityQueryWrapper = new QueryWrapper<>();
            testSuitEntityQueryWrapper.eq("id", inputDto.getTestSuitId());
            testSuitEntityQueryWrapper.eq("is_delete", false);
            existCount = testSuitService.count(testSuitEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属测试套件不存在");
            }
            if(checkMsgs.size()>0){
                responseData = new ResponseData <>();
                responseData.setCode(1);
                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));

                return responseData;
            }

            //DTO转Entity
            TestCaseEntity entity = modelMapper.map(inputDto,TestCaseEntity.class);
            //设置默认值
            entity.setIsDelete(false);
            if(entity.getRequestData() == null){
                entity.setRequestData("{\"headers\":{},\"params\":{},\"data\":{},\"json\":{}}");
            }
            if(entity.getExtract() == null){
                entity.setExtract("[]");
            }
            if(entity.getAssertion() == null){
                entity.setAssertion("[]");
            }
            if(entity.getDbAssertion() == null){
                entity.setDbAssertion("[]");
            }
            if(entity.getOrderIndex() ==null){
                entity.setOrderIndex(10);
            }


            //新增用例
            this.save(entity);

            //Entity转DTO
            TestCaseOutputDto outputDto = modelMapper.map(entity,TestCaseOutputDto.class);

            responseData = ResponseData.success(outputDto);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<TestCaseOutputDto> update(TestCaseUpdateInputDto inputDto) {
        ResponseData<TestCaseOutputDto> responseData;

        try {
            //数据验证
            List<String> checkMsgs = new ArrayList <>();
            //测试用例名称是否存在
            QueryWrapper<TestCaseEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", inputDto.getName());
            queryWrapper.eq("is_delete",false);
            queryWrapper.eq("interface_id",inputDto.getInterfaceId());
            queryWrapper.eq("test_suit_id",inputDto.getTestSuitId());
            queryWrapper.ne("id",inputDto.getId());
            TestCaseEntity testCaseEntity = this.getOne(queryWrapper,false);
            if (testCaseEntity!=null) {
                checkMsgs.add("测试用例名称已经存在");
            }
            //所属接口是否存在
            QueryWrapper<InterfaceEntity> interfaceEntityQueryWrapper = new QueryWrapper<>();
            interfaceEntityQueryWrapper.eq("id", inputDto.getInterfaceId());
            interfaceEntityQueryWrapper.eq("is_delete", false);
            Integer existCount = interfaceService.count(interfaceEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属接口不存在");
            }
            //所属测试套件是否存在
            QueryWrapper<TestSuitEntity> testSuitEntityQueryWrapper = new QueryWrapper<>();
            testSuitEntityQueryWrapper.eq("id", inputDto.getTestSuitId());
            testSuitEntityQueryWrapper.eq("is_delete", false);
            existCount = testSuitService.count(testSuitEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属测试套件不存在");
            }
            if(checkMsgs.size()>0){
                responseData = new ResponseData <>();
                responseData.setCode(1);
                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));

                return responseData;
            }

            //DTO转Entity
            TestCaseEntity entity = modelMapper.map(inputDto,TestCaseEntity.class);

            //修改用例
            this.updateById(entity);


            //Entity转DTO
            TestCaseOutputDto outputDto = modelMapper.map(entity,TestCaseOutputDto.class);

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
            QueryWrapper<TestCaseEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",id);
            queryWrapper.eq("is_delete",false);
            TestCaseEntity testCaseEntity = this.getOne(queryWrapper,false);
            if (testCaseEntity==null) {
                checkMsgs.add("测试用例不存在");
            }
            if(checkMsgs.size()>0){
                responseData = new ResponseData <>();
                responseData.setCode(1);
                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));

                return responseData;
            }

            //修改状态
            testCaseEntity.setIsDelete(true);
            Boolean result = this.updateById(testCaseEntity);

            responseData = ResponseData.success(result);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<TestCaseOutputDto> copy(TestCaseCreateInputDto inputDto) {
        ResponseData<TestCaseOutputDto> responseData;

        try {
            //首先在用例名上添加拷贝的副本名
            inputDto.setName(inputDto.getName()+"-副本");
            //获取所有添加了[-副本]的测试用例名称，循环添加[-副本]比对，防止多次复制
            QueryWrapper<TestCaseEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name", inputDto.getName());
            queryWrapper.eq("is_delete",false);
            queryWrapper.eq("interface_id",inputDto.getInterfaceId());
            queryWrapper.eq("test_suit_id",inputDto.getTestSuitId());
            List<TestCaseEntity> testCaseEntities = this.list(queryWrapper);
            //如果存在相同名称，取名字最长的添加[-副本]
            if (testCaseEntities!=null&& testCaseEntities.size()>0) {
                TestCaseEntity testCaseEntity =  testCaseEntities.stream().max((x,y)->{
                    if(x.getName().length()>y.getName().length()){
                        return 1;
                    }else{
                        return -1;
                    }
                }).get();
                inputDto.setName(testCaseEntity.getName()+"-副本");
            }

            //数据验证
            List<String> checkMsgs = new ArrayList <>();
            //所属接口是否存在
            QueryWrapper<InterfaceEntity> interfaceEntityQueryWrapper = new QueryWrapper<>();
            interfaceEntityQueryWrapper.eq("id", inputDto.getInterfaceId());
            interfaceEntityQueryWrapper.eq("is_delete", false);
            Integer existCount = interfaceService.count(interfaceEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属接口不存在");
            }
            //所属测试套件是否存在
            QueryWrapper<TestSuitEntity> testSuitEntityQueryWrapper = new QueryWrapper<>();
            testSuitEntityQueryWrapper.eq("id", inputDto.getTestSuitId());
            testSuitEntityQueryWrapper.eq("is_delete", false);
            existCount = testSuitService.count(testSuitEntityQueryWrapper);
            if(existCount<=0){
                checkMsgs.add("所属测试套件不存在");
            }
            if(checkMsgs.size()>0){
                responseData = new ResponseData <>();
                responseData.setCode(1);
                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));

                return responseData;
            }

            //DTO转Entity
            TestCaseEntity entity = modelMapper.map(inputDto,TestCaseEntity.class);

            entity.setIsDelete(false);
            //新增用例
            this.save(entity);


            //Entity转DTO
            TestCaseOutputDto outputDto = modelMapper.map(entity,TestCaseOutputDto.class);

            responseData = ResponseData.success(outputDto);
        }catch (Exception ex){
            log.error("操作异常：",ex);
            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

}

