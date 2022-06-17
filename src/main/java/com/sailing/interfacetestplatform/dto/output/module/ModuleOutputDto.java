package com.sailing.interfacetestplatform.dto.output.module;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:27:11
 * @description:模块响应DTO
 **/
@Data
public class ModuleOutputDto implements Serializable {
    private Integer id;
    private String name;
    private Integer projectId;
}
