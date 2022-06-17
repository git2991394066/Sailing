package com.sailing.interfacetestplatform.dto.output.task;

import com.sailing.interfacetestplatform.dto.output.module.ModuleOutputDto;
import lombok.Data;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:30:08
 * @description:测试任务查询响应DTO，下面包含所有关联模块
 **/
@Data
public class TaskQueryOutputDto {
    private Integer id;
    private String name;
    private String description;
    private Integer projectId;
    private Boolean isArchive;
    private Integer archiveId;
    private String archiveName;

    private List<ModuleOutputDto> modules;
}
