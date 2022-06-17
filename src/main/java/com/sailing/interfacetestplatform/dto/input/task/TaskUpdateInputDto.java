package com.sailing.interfacetestplatform.dto.input.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:28:57
 * @description:任务修改输入DTO
 **/
@ApiModel(value = "任务修改输入DTO",description = "描述任务信息")
@Data
public class TaskUpdateInputDto {
    @ApiModelProperty(value = "任务ID",position = 1,example = "99")
    @NotNull(message = "任务ID不能为空")
    private Integer id;
    @ApiModelProperty(value = "任务名称",position = 1,example = "第一轮测试")
    @NotNull(message = "任务名称不能为空")
    private String name;
    @ApiModelProperty(value = "描述",position = 1,example = "描述")
    private String description;
    @ApiModelProperty(value = "所属项目ID",position = 2,example = "99")
    @NotNull(message = "所属项目ID不能为空")
    private Integer projectId;
    @ApiModelProperty(value = "关联模块",position = 3,example = "[]")
    @NotEmpty(message = "关联模块不能为空")
    private List<Integer> moduleIds;
}

