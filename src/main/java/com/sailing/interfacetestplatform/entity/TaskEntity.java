package com.sailing.interfacetestplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:19:30
 * @description:测试任务实体
 **/
@Data
@TableName("tb_task")
public class TaskEntity extends BaseEntity {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private Boolean isDelete;
    private Boolean isArchive;
    private Integer archiveId;
    private String archiveName;
    private Integer projectId;
}
