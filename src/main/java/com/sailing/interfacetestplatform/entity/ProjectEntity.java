package com.sailing.interfacetestplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/15/0015 23:14:19
 * @description:项目实体
 **/
@Data
@TableName("tb_project")
public class ProjectEntity extends BaseEntity {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private Integer leaderId;
    private String leaderName;
    private String description;
    private Boolean isDelete;
}
