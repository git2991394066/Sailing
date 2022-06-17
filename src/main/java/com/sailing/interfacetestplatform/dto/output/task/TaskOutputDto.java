package com.sailing.interfacetestplatform.dto.output.task;

import lombok.Data;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:29:45
 * @description:测试任务响应DTO
 **/
@Data
public class TaskOutputDto {
    private Integer id;
    private String name;
    private String description;
    private Integer projectId;
    private Boolean isArchive;
    private Integer archiveId;
    private String archiveName;

    private List<Integer> moduleIds;
}

