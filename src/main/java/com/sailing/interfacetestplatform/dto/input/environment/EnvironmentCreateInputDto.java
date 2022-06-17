package com.sailing.interfacetestplatform.dto.input.environment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:24:33
 * @description:环境创建输入DTO
 **/
@ApiModel(value = "环境创建输入DTO",description = "描述环境信息")
@Data
public class EnvironmentCreateInputDto {
    @ApiModelProperty(value = "环境名称",position = 1,example = "正式环境")
    @NotNull(message = "环境名称不能为空")
    private String name;
    @ApiModelProperty(value = "主机",position = 2,example = "http://www.test.com")
    @NotNull(message = "主机不能为空")
    private String host;
    @ApiModelProperty(value = "数据库配置",position = 3,example = "jdbc:mysql://www.test.com:3306/testdb")
    @NotNull(message = "数据库配置不能为空")
    private String dbConfig;
    @ApiModelProperty(value = "描述",position = 1,example = "描述")
    private String description;
    @ApiModelProperty(value = "所属项目ID",position = 4,example = "99")
    @NotNull(message = "所属项目ID不能为空")
    private Integer projectId;
}

