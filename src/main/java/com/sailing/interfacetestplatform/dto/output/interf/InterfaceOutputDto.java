package com.sailing.interfacetestplatform.dto.output.interf;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/18/0018 0:47:00
 * @description:项目环境响应DTO
 **/
@Data
public class InterfaceOutputDto implements Serializable {
    private Integer id;
    private String name;
    private String path;
    private String requestMethod;
    private String responseType;
    private Integer developerId;
    private String developerName;
    private Integer moduleId;
    private Integer projectId;
}

