package com.sailing.interfacetestplatform.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 22:31:42
 * @description:实体类基类
 */
@Data
public class BaseEntity implements Serializable {
    @TableField(fill = FieldFill.INSERT)
    private Integer createById;
    @TableField(fill = FieldFill.INSERT)
    private String createByName;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateById;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateByName;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
