package com.sailing.interfacetestplatform.controller;

import com.sailing.interfacetestplatform.annotation.UserRight;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.output.testrecord.TestRecordOutputDto;
import com.sailing.interfacetestplatform.dto.output.testrecord.TestRecordQueryOutputDto;
import com.sailing.interfacetestplatform.service.TestRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/7/14/0014 11:54:20
 * @description:测试记录控制器
 **/
@Api(value="测试记录控制器",tags={"测试记录控制器"})
@RestController
@RequestMapping("/testrecord")
@UserRight(roles = {"admin","staff"})
public class TestRecordController {
    @Autowired
    TestRecordService testRecordsService;

    @ApiOperation(value="测试记录列表", notes="测试记录列表")
    @GetMapping("/")
    public ResponseData<List<TestRecordQueryOutputDto>> query(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, Integer environmentId, Integer taskId, Integer projectId){
        return testRecordsService.query(pageIndex,pageSize,environmentId,taskId,projectId);
    }

    @ApiOperation(value="查询全部", notes="查询全部")
    @GetMapping("/queryByProjectId")
    public ResponseData<List<TestRecordQueryOutputDto>> queryByProjectId(Integer taskId, @RequestParam Integer projectId){
        return testRecordsService.queryByProjectId(taskId,projectId);
    }

    @ApiOperation(value="根据ID获取", notes="根据ID获取")
    @GetMapping("/{id}")
    public ResponseData<TestRecordOutputDto> getById(@PathVariable Integer id){
        return testRecordsService.getById(id);
    }
}

