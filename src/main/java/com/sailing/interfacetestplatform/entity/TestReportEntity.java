package com.sailing.interfacetestplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @auther:张启航Sailling
 * @createDate:2022/7/14/0014 11:24:58
 * @description:测试报告实体，需要在运行后的子线程中进行增、改、查，不能拦截使用会话，所以手动处理创建和修改信息
 **/
@Data
@TableName("tb_test_report")
public class TestReportEntity implements Serializable {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String result;
    private String description;
    private Boolean isDelete;
    private Integer testRecordId;
    private Integer projectId;
//    v1.0.1待定，不好循环提取
//    /**
//     * v1.0.1增加记录自定义字段，便于增加用例响应时间和大小的字段
//     */
//    private String userDefinedResponse;

    private Integer createById;
    private String createByName;
    private Date createTime;
    private Integer updateById;
    private String updateByName;
    private Date updateTime;
}

