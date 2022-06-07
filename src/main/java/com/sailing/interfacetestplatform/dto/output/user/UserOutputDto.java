package com.sailing.interfacetestplatform.dto.output.user;

import com.sailing.interfacetestplatform.dto.output.BaseOutputDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 23:15:08
 * @description:用户操作返回DTO
 **/
@ApiModel(value = "用户信息Output DTO",description = "描述用户详细信息，包含姓名、密码等...")
@Data
public class UserOutputDto extends BaseOutputDto {
    @ApiModelProperty(value = "ID")
    private Integer id;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "电话")
    private String mobile;
    @ApiModelProperty(value = "角色")
    private List<String> roles;
    @ApiModelProperty(value = "描述")
    private String description;
}
