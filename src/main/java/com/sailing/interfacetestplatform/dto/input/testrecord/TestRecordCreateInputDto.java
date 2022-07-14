package com.sailing.interfacetestplatform.dto.input.testrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @auther:张启航Sailling
 * @createDate:2022/7/14/0014 11:18:54
 * @description:测试记录创建输入DTO
 **/
@ApiModel(value = "测试记录创建输入DTO",description = "描述测试记录信息")
@Data
public class TestRecordCreateInputDto {
    @ApiModelProperty(value = "运行记录名称",position = 1)
    @NotNull(message = "运行记录名称不能为空")
    private String name;
    @ApiModelProperty(value = "测试记录状态",position = 1)
    @NotNull
    private Integer status;
    @ApiModelProperty(value = "所属任务ID",position = 4,example = "99")
    @NotNull(message = "所属任务ID不能为空")
    private Integer taskId;
    @ApiModelProperty(value = "所属项目ID",position = 4,example = "99")
    @NotNull(message = "所属项目ID不能为空")
    private Integer projectId;
    @ApiModelProperty(value = "所属环境ID",position = 5,example = "99")
    @NotNull(message = "所属环境ID不能为空")
    private Integer environmentId;
}

