package com.sailing.interfacetestplatform.dto.output.project;

import com.sailing.interfacetestplatform.dto.output.BaseOutputDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/15/0015 23:16:51
 * @description:项目响应DTO
 **/
@Data
public class ProjectOutputDto  extends BaseOutputDto {
    @ApiModelProperty(value = "项目ID",position = 1,example = "99")
    private Integer id;
    @ApiModelProperty(value = "项目名",position = 1,example = "前程贷")
    private String name;
    @ApiModelProperty(value = "负责人Id",position = 2,example = "1")
    private Integer leaderId;
    @ApiModelProperty(value = "负责人姓名",position = 2,example = "李某某")
    private String leaderName;
    @ApiModelProperty(value = "描述",position = 3,example = "这是一个有前途的项目。")
    private String description;
}
