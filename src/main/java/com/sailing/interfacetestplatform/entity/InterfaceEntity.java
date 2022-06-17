package com.sailing.interfacetestplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/18/0018 0:41:05
 * @description:项目接口实体
 **/
@Data
@TableName("tb_interface")
public class InterfaceEntity extends BaseEntity {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private String path;
    private String requestMethod;
    private String responseType;
    private Integer developerId;
    private String developerName;
    private String description;
    private Boolean isDelete;
    private Integer moduleId;
    private Integer projectId;
}

