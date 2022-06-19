package com.sailing.interfacetestplatform.dto.output.task;

import com.sailing.interfacetestplatform.dto.output.testsuit.TestSuitDetailOutputDto;
import lombok.Data;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:29:23
 * @description:测试任务详情响应DTO，下面包含所有关联测试套件列表
 **/
@Data
public class TaskDetailOutputDto {
    private Integer id;
    private String name;
    private String description;
    private Integer projectId;
    private Boolean isArchive;
    private Integer archiveId;
    private String archiveName;
    private List<TestSuitDetailOutputDto> testSuits;
}

