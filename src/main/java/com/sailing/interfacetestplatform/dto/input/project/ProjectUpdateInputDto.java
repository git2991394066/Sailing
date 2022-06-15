package com.sailing.interfacetestplatform.dto.input.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/15/0015 23:15:58
 * @description:项目更新输入DTO
 **/
@ApiModel(value = "项目更新输入DTO",description = "描述项目信息")
@Data
public class ProjectUpdateInputDto {
    @ApiModelProperty(value = "项目ID",position = 1,example = "99")
    @NotNull
    private Integer id;
    @ApiModelProperty(value = "项目名称",position = 1,example = "前程贷")
    @NotNull(message = "项目名称不能为空")
    private String name;
    @ApiModelProperty(value = "负责人Id",position = 2,example = "1")
//    @NotNull(message = "负责人不能为空")
    private Integer leaderId;
    @ApiModelProperty(value = "负责人姓名",position = 2,example = "李某某")
    @NotNull(message = "负责人不能为空")
    private String leaderName;
    @ApiModelProperty(value = "描述",position = 3,example = "这是一个有前途的项目。")
    private String description;
}
