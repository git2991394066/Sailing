package com.sailing.interfacetestplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 0:49:23
 * @description:测试套件实体
 **/
@Data
@TableName("tb_test_suit")
public class TestSuitEntity extends BaseEntity {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private Boolean isDelete;
    private Integer taskId;
    private Integer projectId;
}

