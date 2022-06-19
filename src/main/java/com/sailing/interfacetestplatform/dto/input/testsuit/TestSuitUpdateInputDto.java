package com.sailing.interfacetestplatform.dto.input.testsuit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 0:51:05
 * @description:测试套件修改输入DTO
 **/
@ApiModel(value = "测试套件修改输入DTO",description = "描述测试套件信息")
@Data
public class TestSuitUpdateInputDto {
    @ApiModelProperty(value = "测试套件ID",position = 1,example = "99")
    @NotNull(message = "测试套件ID不能为空")
    private Integer id;
    @ApiModelProperty(value = "测试套件名称",position = 1,example = "借贷测试套件")
    @NotNull(message = "测试套件名称不能为空")
    private String name;
    @ApiModelProperty(value = "所属任务ID",position = 2,example = "99")
    @NotNull(message = "所属任务ID不能为空")
    private Integer taskId;
    @ApiModelProperty(value = "所属项目ID",position = 2,example = "99")
    @NotNull(message = "所属项目ID不能为空")
    private Integer projectId;
}

