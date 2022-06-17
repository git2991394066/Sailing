package com.sailing.interfacetestplatform.dto.output.environment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:25:25
 * @description:项目环境响应DTO
 **/
@ApiModel(value = "项目环境响应DTO",description = "描述环境信息")
@Data
public class EnvironmentOutputDto implements Serializable {
    @ApiModelProperty(value = "环境ID")
    private Integer id;
    @ApiModelProperty(value = "环境名称")
    private String name;
    @ApiModelProperty(value = "主机")
    private String host;
    @ApiModelProperty(value = "数据库配置")
    private String dbConfig;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "所属项目ID")
    private Integer projectId;
}

