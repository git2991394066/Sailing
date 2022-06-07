package com.sailing.interfacetestplatform.dto.output.account;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 23:13:05
 * @description:登录返回DTO
 **/
@Data
public class LoginOutputDto implements Serializable {
    private Integer id;
    private String username;
    private String name;
    private List<String> roles;
}
