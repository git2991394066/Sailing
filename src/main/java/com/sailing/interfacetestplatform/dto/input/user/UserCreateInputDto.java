package com.sailing.interfacetestplatform.dto.input.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 23:10:46
 * @description:用户创建输入DTO
 **/
@ApiModel(value = "用户创建传入DTO",description = "用户创建传入信息DTO")
@Data
public class UserCreateInputDto {
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    @NotNull(message = "密码不能为空")
    @Length(min = 6,max = 20,message = "密码长度必须在6-20个字符之间")
    private String password;
    @ApiModelProperty(value = "姓名")
    @NotNull(message = "姓名不能为空")
    private String name;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "电话")
    private String mobile;
    @ApiModelProperty(value = "角色")
    @NotEmpty(message = "角色不能为空")
    private List<String> roles;
    @ApiModelProperty(value = "描述")
    private String description;
}
