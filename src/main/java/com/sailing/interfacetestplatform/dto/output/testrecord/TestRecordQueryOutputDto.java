package com.sailing.interfacetestplatform.dto.output.testrecord;

import com.sailing.interfacetestplatform.dto.output.BaseOutputDto;
import com.sailing.interfacetestplatform.dto.output.task.TaskOutputDto;
import com.sailing.interfacetestplatform.dto.output.testreport.TestReportOutputDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auther:张启航Sailling
 * @createDate:2022/7/14/0014 11:20:13
 * @description:测试记录查询响应DTO
 **/
@ApiModel(value = "测试记录查询响应DTO",description = "描述测试记录信息")
@Data
public class TestRecordQueryOutputDto extends BaseOutputDto {
    @ApiModelProperty(value = "测试记录ID",position = 1,example = "99")
    private Integer id;
    @ApiModelProperty(value = "运行记录名称",position = 1,example = "0.1")
    private String name;
    @ApiModelProperty(value = "测试记录状态",position = 1)
    private Integer status;
    @ApiModelProperty(value = "所属任务ID",position = 4,example = "99")
    private Integer taskId;
    @ApiModelProperty(value = "所属项目ID",position = 4,example = "99")
    private Integer projectId;
    @ApiModelProperty(value = "所属环境ID",position = 5,example = "99")
    private Integer environmentId;

    private TaskOutputDto task;

    private TestReportOutputDto testReport;
}

