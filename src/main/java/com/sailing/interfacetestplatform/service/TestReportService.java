package com.sailing.interfacetestplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.testreport.TestReportCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.testreport.TestReportUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.testreport.TestReportOutputDto;
import com.sailing.interfacetestplatform.entity.TestReportEntity;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/7/14/0014 11:30:16
 * @description:测试报告服务接口
 **/
public interface TestReportService extends IService<TestReportEntity> {
    /**
     * 分页查询列表
     * @param pageIndex
     * @param pageSize
     * * @param moduleId
     * @param projectId
     * @return
     */
    ResponseData<List<TestReportOutputDto>> query(Integer pageIndex, Integer pageSize, Integer testRecordId, Integer projectId);

    /**
     * 查询所有
     * @return
     */
    ResponseData<List<TestReportOutputDto>> queryByProjectId(Integer projectId);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    ResponseData<TestReportOutputDto> getById(Integer id);

    /**
     * 创建
     * @param inputDto
     * @return
     */
    ResponseData<TestReportOutputDto> create(TestReportCreateInputDto inputDto);

    /**
     * 更新
     * @param inputDto
     * @return
     */
    ResponseData<TestReportOutputDto> update(TestReportUpdateInputDto inputDto);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    ResponseData<Boolean> delete(Integer id);
}
