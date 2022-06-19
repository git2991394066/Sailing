package com.sailing.interfacetestplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 1:24:22
 * @description:测试任务-模块关联实体
 **/
@Data
@TableName("tb_task_module")
public class TaskModuleEntity implements Serializable {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private Integer taskId;
    private Integer moduleId;
}
