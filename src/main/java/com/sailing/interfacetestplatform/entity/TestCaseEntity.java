package com.sailing.interfacetestplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 0:39:50
 * @description:测试用例实体
 **/
@Data
@TableName("tb_test_case")
public class TestCaseEntity extends BaseEntity {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private String marks;
    private String requestData;
    private String description;
    private String assertion;
    private String dbAssertion;
    private String extract;
    private Integer orderIndex;
    private Boolean isDelete;
    private Integer interfaceId;
    private Integer moduleId;
    private Integer testSuitId;
    private Integer taskId;
    private Integer projectId;
}

