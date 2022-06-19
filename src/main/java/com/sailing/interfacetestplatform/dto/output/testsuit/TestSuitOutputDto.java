package com.sailing.interfacetestplatform.dto.output.testsuit;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 0:55:32
 * @description:测试套件响应DTO
 **/
@Data
public class TestSuitOutputDto implements Serializable {
    private Integer id;
    private String name;
    private Integer taskId;
    private Integer projectId;
}