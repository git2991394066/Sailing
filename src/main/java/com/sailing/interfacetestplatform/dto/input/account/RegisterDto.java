package com.sailing.interfacetestplatform.dto.input.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 23:06:31
 * @description:注册传入DTO
 **/
@ApiModel(description = "注册传入DTO")
@Data
public class RegisterDto {
    @ApiModelProperty(value = "用户名",position = 1)
    @NotNull(message = "用户名不能为空")
    @Length(min = 5,max = 20,message = "用户名长度必须在5-20个字符之间")
    private String username;
    @ApiModelProperty(value = "密码",position = 2)
    @NotNull(message = "密码不能为空")
    @Length(min = 6,max = 20,message = "密码长度必须在6-20个字符之间")
    private String password;
    @ApiModelProperty(value = "姓名",position = 3)
    @NotNull(message = "姓名不能为空")
    private String name;
}
