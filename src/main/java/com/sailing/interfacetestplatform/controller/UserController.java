package com.sailing.interfacetestplatform.controller;

import com.sailing.interfacetestplatform.annotation.UserRight;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.user.UserCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.user.UserResetPasswordInputDto;
import com.sailing.interfacetestplatform.dto.input.user.UserUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.user.UserOutputDto;
import com.sailing.interfacetestplatform.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/8/0008 23:59:24
 * @description:用户控制器
 **/
@Api(value = "用户接口",tags = {"用户接口"})
@RestController
@RequestMapping("/user")
@UserRight(roles = {"admin"})
public class UserController {
    @Autowired
    UserService userService;
    @ApiOperation(value = "分页查询",notes = "分页查询数据")
    @GetMapping("/")
    ResponseData<List<UserOutputDto>> query(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, String username, String name){
        return userService.query(pageIndex,pageSize,username,name);
    }
    @ApiOperation(value = "查询所有数据",notes = "查询所有数据")
    @GetMapping("/queryAll")
    @UserRight(roles = {"admin","staff"})
    ResponseData<List<UserOutputDto>> queryAll(){
        return userService.queryAll();
    }

    @ApiOperation(value = "根据ID获取数据",notes = "根据ID获取数据详情")
    @GetMapping("/{id}")
    ResponseData<UserOutputDto> getById(@PathVariable Integer id){
        return userService.getById(id);
    }

    @ApiOperation(value = "创建",notes = "创建一条新数据")
    @PostMapping("/")
    ResponseData<UserOutputDto> create(@RequestBody @Validated UserCreateInputDto inputDto){
        return userService.create(inputDto);
    }

    @ApiOperation(value = "修改",notes = "修改一条已存在数据")
    @PutMapping("/")
    ResponseData<UserOutputDto> update(@RequestBody @Validated UserUpdateInputDto inputDto){
        return  userService.update(inputDto);
    }

    @ApiOperation(value = "删除",notes = "删除一条已存在数据")
    @DeleteMapping("/{id}")
    ResponseData<Boolean> delete(@PathVariable Integer id){
        return userService.delete(id);
    }

    @ApiOperation(value = "重置密码",notes = "重置密码")
    @PostMapping("/resetPassword")
    public ResponseData<Boolean> resetPassword(@RequestBody @Validated UserResetPasswordInputDto loginDto){
        return userService.resetPassord(loginDto);
    }
}
