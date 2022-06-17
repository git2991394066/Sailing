package com.sailing.interfacetestplatform.controller;

import com.sailing.interfacetestplatform.annotation.UserRight;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.module.ModuleCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.module.ModuleUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.module.ModuleOutputDto;
import com.sailing.interfacetestplatform.service.ModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/17/0017 1:41:31
 * @description:模块控制器
 **/
@Api(value="模块控制器",tags={"模块控制器"})
@RestController
@RequestMapping("/module")
@UserRight(roles = {"admin","staff"})
public class ModuleController {
    @Autowired
    ModuleService modulesService;

    @ApiOperation(value="按条件分页查询", notes="按条件分页查询")
    @GetMapping("/")
    public ResponseData<List<ModuleOutputDto>> query(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Integer projectId){
        return modulesService.query(pageIndex,pageSize,projectId);
    }

    @ApiOperation(value="查询全部", notes="查询全部")
    @GetMapping("/queryByProjectId")
    public ResponseData<List<ModuleOutputDto>> queryByProjectId(@RequestParam Integer projectId){
        return modulesService.queryByProjectId(projectId);
    }

    @ApiOperation(value="根据ID获取模块", notes="根据ID获取模块")
    @GetMapping("/{id}")
    public ResponseData<ModuleOutputDto> getById(@PathVariable Integer id){
        return modulesService.getById(id);
    }

    @ApiOperation(value="创建模块", notes="创建模块")
    @PostMapping("/")
    public ResponseData<ModuleOutputDto> create(@RequestBody @Validated ModuleCreateInputDto inputDto){
        return modulesService.create(inputDto);
    }

    @ApiOperation(value="修改模块", notes="修改模块")
    @PutMapping("/")
    public ResponseData<ModuleOutputDto> update(@RequestBody @Validated ModuleUpdateInputDto inputDto){
        return modulesService.update(inputDto);
    }

    @ApiOperation(value="根据ID删除模块", notes="根据ID删除模块")
    @DeleteMapping("/{id}")
    public ResponseData<Boolean> delete(@PathVariable Integer id){
        return modulesService.delete(id);
    }
}

