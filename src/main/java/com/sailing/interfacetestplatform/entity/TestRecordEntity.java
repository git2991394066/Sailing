package com.sailing.interfacetestplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @auther:张启航Sailling
 * @createDate:2022/7/14/0014 11:17:46
 * @description:测试记录实体，需要在运行后的子线程中进行增、改、查，不能拦截使用会话，所以手动处理创建和修改信息
 **/
@Data
@TableName("tb_test_record")
public class TestRecordEntity implements Serializable {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private Integer status;
    private Boolean isDelete;
    private Integer taskId;
    private Integer projectId;
    private Integer environmentId;

    private Integer createById;
    private String createByName;
    private Date createTime;
    private Integer updateById;
    private String updateByName;
    private Date updateTime;
}
