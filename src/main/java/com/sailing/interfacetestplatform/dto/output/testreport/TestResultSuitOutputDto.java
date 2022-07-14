package com.sailing.interfacetestplatform.dto.output.testreport;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/24/0024 23:00:15
 * @description:测试结果的测试套件响应DTO，下面包含关联的测试结果用例列表信息
 **/
@Data
public class TestResultSuitOutputDto implements Serializable {
    private Integer testSuitId;
    private String testSuitName;

    private List<TestResultCaseOutputDto> testCaseResults;
}
