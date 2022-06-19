package com.sailing.interfacetestplatform.dto.input.testcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 0:41:22
 * @description:测试用例创建输入DTO
 **/
@ApiModel(value = "测试用例创建输入DTO",description = "描述测试用例信息")
@Data
public class TestCaseCreateInputDto {
    @ApiModelProperty(value = "测试用例名称",position = 1,example = "登录测试用例")
    @NotNull(message = "测试用例名称不能为空")
    private String name;
    @ApiModelProperty(value = "请求数据",position = 4)
    private String requestData;
    @ApiModelProperty(value = "标记",position = 6,example = "贷款，贷款，余额")
    private String marks;
    @ApiModelProperty(value = "断言",position = 9)
    private String assertion;
    @ApiModelProperty(value = "数据库断言",position = 10)
    private String dbAssertion;
    @ApiModelProperty(value = "提取数据",position = 11)
    private String extract;
    @ApiModelProperty(value = "运行优先级",position = 12)
    private Integer orderIndex;
    @ApiModelProperty(value = "所属接口ID",position = 12,example = "99")
    @NotNull(message = "所属接口ID不能为空")
    private Integer interfaceId;
    @ApiModelProperty(value = "所属模块ID",position = 12,example = "99")
    @NotNull(message = "所属模块ID不能为空")
    private Integer moduleId;
    @ApiModelProperty(value = "所属测试套件ID",position = 13,example = "99")
    @NotNull(message = "所属测试套件ID不能为空")
    private Integer testSuitId;
    @ApiModelProperty(value = "所属任务ID",position = 13,example = "99")
    @NotNull(message = "所属任务ID不能为空")
    private Integer taskId;
    @ApiModelProperty(value = "所属项目ID",position = 13,example = "99")
    @NotNull(message = "所属项目ID不能为空")
    private Integer projectId;
    @ApiModelProperty(value = "描述",position = 14)
    private String description;
}
