package com.sailing.interfacetestplatform.dto.input.module;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:26:25
 * @description:模块创建输入DTO
 **/
@ApiModel(value = "模块创建输入DTO",description = "描述模块信息")
@Data
public class ModuleCreateInputDto {
    @ApiModelProperty(value = "模块名称",position = 1,example = "借贷模块")
    @NotNull(message = "模块名称不能为空")
    private String name;
    @ApiModelProperty(value = "描述",position = 1,example = "描述")
    private String description;
    @ApiModelProperty(value = "所属项目ID",position = 2,example = "99")
    @NotNull(message = "所属项目ID不能为空")
    private Integer projectId;
}
