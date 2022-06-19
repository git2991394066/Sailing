package com.sailing.interfacetestplatform.dto.output.testsuit;

import com.sailing.interfacetestplatform.dto.output.testcase.TestCaseOutputDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 0:51:42
 * @description:测试套件详情响应DTO，下面包含关联的测试用例列表信息
 **/
@Data
public class TestSuitDetailOutputDto implements Serializable {
    private Integer id;
    private String name;
    private Integer taskId;
    private Integer projectId;
    private List<TestCaseOutputDto> testCases;
}

