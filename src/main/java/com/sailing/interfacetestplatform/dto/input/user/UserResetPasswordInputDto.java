package com.sailing.interfacetestplatform.dto.input.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 23:11:23
 * @description:重置密码输入Dto
 **/
@ApiModel(value = "用户修改传入DTO",description = "用户修改传入信息DTO")
@Data
public class UserResetPasswordInputDto {
    @ApiModelProperty(value = "ID",example = "1")
    @NotNull(message = "用户ID不能为空")
    private Integer id;
    @ApiModelProperty(value = "密码")
    @NotNull(message = "密码不能为空")
    @Length(min = 6,max = 20,message = "密码长度必须在6-20个字符之间")
    private String password;
}
