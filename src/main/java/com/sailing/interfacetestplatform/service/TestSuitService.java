package com.sailing.interfacetestplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.testsuit.TestSuitCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.testsuit.TestSuitUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.testsuit.TestSuitOutputDto;
import com.sailing.interfacetestplatform.entity.TestSuitEntity;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 0:52:22
 * @description:测试套件服务接口
 **/
public interface TestSuitService extends IService<TestSuitEntity> {
    /**
     * 查询所有
     * @return
     */
    ResponseData<List<TestSuitOutputDto>> queryByProjectId(Integer projectId);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    ResponseData<TestSuitOutputDto> getById(Integer id);

    /**
     * 创建
     * @param inputDto
     * @return
     */
    ResponseData<TestSuitOutputDto> create(TestSuitCreateInputDto inputDto);

    /**
     * 更新
     * @param inputDto
     * @return
     */
    ResponseData<TestSuitOutputDto> update(TestSuitUpdateInputDto inputDto);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    ResponseData<Boolean> delete(Integer id);
}
