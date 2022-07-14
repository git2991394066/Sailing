package com.sailing.interfacetestplatform.dto.output.testreport;

import com.sailing.interfacetestplatform.dto.output.environment.EnvironmentOutputDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/24/0024 22:59:43
 * @description:测试结果响应DTO，下面包含关联的测试用例集合、项目环境信息
 **/
@Data
public class TestResultOutputDto implements Serializable {
    private Integer taskId;
    private String taskName;
    private Double totalDuration;
    private Integer status;
    private Long totalOfTestSuit;
    private Long totalOfTestCase;
    private Long totalOfTestCaseForSuccess;
    private Long totalOfTestCaseForFailure;
    private Long totalOfTestCaseForError;

    private List<TestResultSuitOutputDto> testSuitResults;
    private EnvironmentOutputDto environment;
}
