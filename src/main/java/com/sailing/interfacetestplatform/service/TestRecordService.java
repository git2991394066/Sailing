package com.sailing.interfacetestplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.testrecord.TestRecordCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.testrecord.TestRecordUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.testrecord.TestRecordOutputDto;
import com.sailing.interfacetestplatform.dto.output.testrecord.TestRecordQueryOutputDto;
import com.sailing.interfacetestplatform.entity.TestRecordEntity;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/7/14/0014 11:15:30
 * @description:测试记录服务接口
 **/
public interface TestRecordService extends IService<TestRecordEntity> {
    /**
     * 分页查询列表
     * @param pageIndex
     * @param pageSize
     * * @param moduleId
     * @param projectId
     * @return
     */
    ResponseData<List<TestRecordQueryOutputDto>> query(Integer pageIndex, Integer pageSize, Integer enviromentId, Integer taskId, Integer projectId);

    /**
     * 查询所有
     * @return
     */
    ResponseData<List<TestRecordQueryOutputDto>> queryByProjectId(Integer taskId,Integer projectId);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    ResponseData<TestRecordOutputDto> getById(Integer id);

    /**
     * 创建
     * @param inputDto
     * @return
     */
    ResponseData<TestRecordOutputDto> create(TestRecordCreateInputDto inputDto);

    /**
     * 更新
     * @param inputDto
     * @return
     */
    ResponseData<TestRecordOutputDto> update(TestRecordUpdateInputDto inputDto);
}

