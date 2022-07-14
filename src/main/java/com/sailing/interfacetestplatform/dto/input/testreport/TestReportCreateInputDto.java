package com.sailing.interfacetestplatform.dto.input.testreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @auther:张启航Sailling
 * @createDate:2022/7/14/0014 11:26:34
 * @description:测试报告创建输入DTO
 **/
@ApiModel(value = "测试报告创建输入DTO",description = "描述测试报告信息")
@Data
public class TestReportCreateInputDto {
    @ApiModelProperty(value = "执行结果",position = 2,example = "")
    @NotNull
    private String result;
    @ApiModelProperty(value = "所属测试记录ID",position = 3,example = "99")
    @NotNull
    private Integer testRecordId;
    @ApiModelProperty(value = "所属项目ID",position = 3,example = "99")
    @NotNull
    private Integer projectId;
}

