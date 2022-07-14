package com.sailing.interfacetestplatform.dto.output.testreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @auther:张启航Sailling
 * @createDate:2022/7/14/0014 11:28:47
 * @description:测试报告响应DTO
 **/
@ApiModel(value = "测试报告响应DTO",description = "描述测试报告信息")
@Data
public class TestReportOutputDto {
    @ApiModelProperty(value = "测试报告ID",position = 1)
    @NotNull
    private Integer id;
    @ApiModelProperty(value = "测试报告状态",position = 2)
    private Integer status;
    @ApiModelProperty(value = "执行结果",position = 3)
    private String result;
    @ApiModelProperty(value = "所属测试记录ID",position = 4)
    private Integer testRecordId;
    @ApiModelProperty(value = "所属项目ID",position = 5)
    private Integer projectId;
    @ApiModelProperty(value = "测试用例总数",position = 6)
    private Long totalOfTestCase;
    @ApiModelProperty(value = "测试成功用例数",position = 7)
    private Long totalOfTestCaseForSuccess;
    @ApiModelProperty(value = "测试失败用例数",position = 8)
    private Long totalOfTestCaseForFailure;
    @ApiModelProperty(value = "测试错误用例数",position = 9)
    private Long totalOfTestCaseForError;
}

