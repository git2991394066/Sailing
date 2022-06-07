package com.sailing.interfacetestplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 22:34:43
 * @description:用户实体类
 */
@Data
//映射名称，如果表名与类名不同，使用value指定表名；autoResultMap设置非基本类型的handler提示
@TableName(value = "tb_user",autoResultMap = true)
public class UserEntity extends BaseEntity{
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String mobile;
    private Date lastLoginTime;
    private Boolean isDelete;
    private Integer currentProjectId;
    private String description;
    //使用List转Varchar
    @TableField(jdbcType = JdbcType.VARCHAR,typeHandler = ListTypeHandler.class)
    private List<String> roles;
}
