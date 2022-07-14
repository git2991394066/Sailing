package com.sailing.interfacetestplatform.dto.output.testrecord;

import com.sailing.interfacetestplatform.dto.output.BaseOutputDto;
import com.sailing.interfacetestplatform.dto.output.testreport.TestReportOutputDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auther:张启航Sailling
 * @createDate:2022/7/14/0014 11:19:49
 * @description:测试记录响应DTO
 **/
@ApiModel(value = "测试记录响应DTO",description = "描述测试记录信息")
@Data
public class TestRecordOutputDto extends BaseOutputDto {
    @ApiModelProperty(value = "测试记录ID",position = 1)
    private Integer id;
    @ApiModelProperty(value = "运行记录名称",position = 2)
    private String name;
    @ApiModelProperty(value = "测试记录状态",position = 3)
    private Integer status;
    @ApiModelProperty(value = "所属任务ID",position = 4)
    private Integer taskId;
    @ApiModelProperty(value = "所属项目ID",position = 5)
    private Integer projectId;
    @ApiModelProperty(value = "所属环境ID",position = 6)
    private Integer environmentId;
    @ApiModelProperty(value = "关联测试报告",position = 7)
    private TestReportOutputDto testReport;

}

