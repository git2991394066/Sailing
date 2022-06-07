package com.sailing.interfacetestplatform.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 22:58:57
 * @description:Dto基类
 **/
@Data
public class BaseOutputDto implements Serializable {
    private Integer createById;
    private String createByName;
    private Date createTime;
    private Integer updateById;
    private String updateByName;
    private Date updateTime;
}
