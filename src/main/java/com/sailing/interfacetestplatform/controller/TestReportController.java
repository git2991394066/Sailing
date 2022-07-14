package com.sailing.interfacetestplatform.controller;

import com.sailing.interfacetestplatform.annotation.UserRight;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.output.testreport.TestReportOutputDto;
import com.sailing.interfacetestplatform.service.TestReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther:张启航Sailling
 * @createDate:2022/7/14/0014 11:54:48
 * @description:测试报告控制器
 **/
@Api(value="测试报告控制器",tags={"测试报告控制器"})
@RestController
@RequestMapping("/testreport")
@UserRight(roles = {"admin","staff"})
public class TestReportController {
    @Autowired
    TestReportService testReportService;

    @ApiOperation(value="根据ID获取", notes="根据ID获取")
    @GetMapping("/{id}")
    public ResponseData<TestReportOutputDto> getById(@PathVariable Integer id){
        return testReportService.getById(id);
    }
}

