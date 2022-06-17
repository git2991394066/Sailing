package com.sailing.interfacetestplatform.dto.input.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:28:32
 * @description:任务运行输入DTO
 **/
@ApiModel(value = "任务运行Dto",description = "描述任务运行输入信息")
@Data
public class TaskRunInputDto {
    @ApiModelProperty(value = "测试任务ID",position = 1,example = "99")
    @NotNull(message = "测试任务ID不能为空")
    private Integer taskId;
    @ApiModelProperty(value = "项目ID",position = 1,example = "99")
    @NotNull(message = "项目ID不能为空")
    private Integer projectId;
    @ApiModelProperty(value = "项目环境ID",position = 2,example = "99")
    @NotNull(message = "项目环境ID不能为空")
    private Integer environmentId;
    @ApiModelProperty(value = "测试记录名称",position = 3,example = "1.0")
    private String name;
}

