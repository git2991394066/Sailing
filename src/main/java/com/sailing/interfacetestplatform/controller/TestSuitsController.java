package com.sailing.interfacetestplatform.controller;

import com.sailing.interfacetestplatform.annotation.UserRight;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.testsuit.TestSuitCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.testsuit.TestSuitUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.testsuit.TestSuitOutputDto;
import com.sailing.interfacetestplatform.service.TestSuitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 0:53:42
 * @description:测试套件控制器
 **/
@Api(value="测试套件控制器",tags={"测试套件控制器"})
@RestController
@RequestMapping("/testsuit")
@UserRight(roles = {"admin","staff"})
public class TestSuitsController {
    @Autowired
    TestSuitService testSuitsService;

    @ApiOperation(value="查询全部", notes="查询全部")
    @GetMapping("/queryByProjectId")
    public ResponseData<List<TestSuitOutputDto>> queryByProjectId(@RequestParam Integer projectId){
        return testSuitsService.queryByProjectId(projectId);
    }

    @ApiOperation(value="根据ID获取", notes="根据ID获取")
    @GetMapping("/{id}")
    public ResponseData<TestSuitOutputDto> getById(@PathVariable Integer id){
        return testSuitsService.getById(id);
    }

    @ApiOperation(value="创建接口", notes="创建接口")
    @PostMapping("/")
    public ResponseData<TestSuitOutputDto> create(@RequestBody @Validated TestSuitCreateInputDto inputDto){
        return testSuitsService.create(inputDto);
    }

    @ApiOperation(value="修改接口", notes="修改接口")
    @PutMapping("/")
    public ResponseData<TestSuitOutputDto> update(@RequestBody @Validated TestSuitUpdateInputDto inputDto){
        return testSuitsService.update(inputDto);
    }

    @ApiOperation(value="根据ID删除接口", notes="根据ID删除接口")
    @DeleteMapping("/{id}")
    public ResponseData<Boolean> delete(@PathVariable Integer id){
        return testSuitsService.delete(id);
    }
}

