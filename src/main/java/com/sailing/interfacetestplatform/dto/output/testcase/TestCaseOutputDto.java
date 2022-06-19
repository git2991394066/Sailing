package com.sailing.interfacetestplatform.dto.output.testcase;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 0:42:41
 * @description:测试用例响应DTO
 **/
@Data
public class TestCaseOutputDto implements Serializable {
    private Integer id;
    private String name;
    private String requestData;
    private String marks;
    private String assertion;
    private String dbAssertion;
    private String extract;
    private Integer orderIndex;
    private Integer interfaceId;
    private Integer moduleId;
    private Integer testSuitId;
    private Integer taskId;
    private Integer projectId;
    private String description;
}

