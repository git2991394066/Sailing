package com.sailing.interfacetestplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:18:23
 * @description:项目环境实体
 **/
@Data
@TableName("tb_environment")
public class EnvironmentEntity extends BaseEntity {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private String host;
    private String dbConfig;
    private String description;
    private Boolean isDelete;
    private Integer projectId;
}

