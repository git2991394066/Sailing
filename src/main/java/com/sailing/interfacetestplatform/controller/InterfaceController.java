package com.sailing.interfacetestplatform.controller;

import com.sailing.interfacetestplatform.annotation.UserRight;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.interf.InterfaceCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.interf.InterfaceUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.interf.InterfaceOutputDto;
import com.sailing.interfacetestplatform.dto.output.interf.InterfaceQueryOutputDto;
import com.sailing.interfacetestplatform.service.InterfaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/18/0018 0:51:38
 * @description:接口控制器
 **/
@Api(value="接口控制器",tags={"接口控制器"})
@RestController
@RequestMapping("/interface")
@UserRight(roles = {"admin","staff"})
public class InterfaceController {
    @Autowired
    InterfaceService interfacesService;

    @ApiOperation(value="按条件分页查询", notes="按条件分页查询")
    @GetMapping("/")
    public ResponseData<List<InterfaceQueryOutputDto>> query(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, Integer moduleId, @RequestParam Integer projectId){
        return interfacesService.query(pageIndex,pageSize,moduleId,projectId);
    }

    @ApiOperation(value="查询全部", notes="查询全部")
    @GetMapping("/queryByProjectId")
    public ResponseData<List<InterfaceOutputDto>> queryByProjectId(@RequestParam Integer projectId){
        return interfacesService.queryByProjectId(projectId);
    }

    @ApiOperation(value="根据ID获取", notes="根据ID获取")
    @GetMapping("/{id}")
    public ResponseData<InterfaceOutputDto> getById(@PathVariable Integer id){
        return interfacesService.getById(id);
    }

    @ApiOperation(value="创建接口", notes="创建接口")
    @PostMapping("/")
    public ResponseData<InterfaceOutputDto> create(@RequestBody @Validated InterfaceCreateInputDto inputDto){
        return interfacesService.create(inputDto);
    }

    @ApiOperation(value="修改接口", notes="修改接口")
    @PutMapping("/")
    public ResponseData<InterfaceOutputDto> update(@RequestBody @Validated InterfaceUpdateInputDto inputDto){
        return interfacesService.update(inputDto);
    }

    @ApiOperation(value="根据ID删除接口", notes="根据ID删除接口")
    @DeleteMapping("/{id}")
    public ResponseData<Boolean> delete(@PathVariable Integer id){
        return interfacesService.delete(id);
    }
}

