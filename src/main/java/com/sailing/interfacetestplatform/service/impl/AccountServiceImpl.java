package com.sailing.interfacetestplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.account.LoginInputDto;
import com.sailing.interfacetestplatform.dto.input.account.RegisterDto;
import com.sailing.interfacetestplatform.dto.output.account.LoginOutputDto;
import com.sailing.interfacetestplatform.entity.UserEntity;
import com.sailing.interfacetestplatform.mapper.UserMapper;
import com.sailing.interfacetestplatform.service.AccountService;
import com.sailing.interfacetestplatform.util.JWTUtil;
import com.sailing.interfacetestplatform.util.PasswordUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/8/0008 23:52:35
 * @description:
 **/
@Service
public class AccountServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements AccountService {
//    @Autowired
//    SessionUtil sessionUtil;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public ResponseData<LoginOutputDto> login(LoginInputDto inputDto) {
        ResponseData<LoginOutputDto> responseData;

        try{
            //根据用户名查询用户信息
            QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", inputDto.getUsername().trim());
            queryWrapper.eq("is_delete",false);
            UserEntity tbUser = this.getOne(queryWrapper,false);
            if(tbUser !=null){
                if(tbUser.getPassword().equalsIgnoreCase(PasswordUtil.encrypt(inputDto.getPassword(),inputDto.getUsername().trim())) == false){

                    responseData = new ResponseData<>();
                    responseData.setCode(3);
                    responseData.setMessage("密码不正确");

                    return responseData;
                }
            }else{
                responseData = new ResponseData<>();
                responseData.setCode(2);
                responseData.setMessage("用户名不存在");

                return responseData;
            }

            LoginOutputDto loginOutputDto = modelMapper.map(tbUser,LoginOutputDto.class);

//            //保存登录用户到会话，使用会话形式
//            SessionUtil.CurrentUser currentUser = new SessionUtil.CurrentUser();
//            currentUser.setUserEntity(tbUser);
//            sessionUtil.setCurrentUser(currentUser);

            //【JWT】3、登录时返回token，使用JWT Token形式
            //验证通过,返回token
            HashMap<String, String> tokenInfos = new HashMap<>();
            tokenInfos.put("status","success");
            //将返回数据同时放到token中，用于认证与授权时使用token中的信息进行验证
            tokenInfos.put("data",new ObjectMapper().writeValueAsString(loginOutputDto));

            //添加token
            String token = JWTUtil.generateToken(tokenInfos);
            loginOutputDto.setToken(token);

            responseData = ResponseData.success(loginOutputDto);
        }catch (Exception ex){
            log.error("操作异常",ex);
            responseData = ResponseData.failure(ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<Boolean> register(RegisterDto registerDto) {
        ResponseData<Boolean> responseData;
        try{
            LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
            //查询条件
            queryWrapper.eq(UserEntity::getUsername, registerDto.getUsername());
            UserEntity existUser = this.getOne(queryWrapper,false);
            if(existUser ==null){
                UserEntity userEntity = modelMapper.map(registerDto,UserEntity.class);
                //注册用户都为普通用户
                List<String> roles = new ArrayList<>();
                roles.add("staff");
                userEntity.setRoles(roles);
                //描述特别处理
                userEntity.setDescription("[register]");
                //密码加密
                String password = PasswordUtil.encrypt(registerDto.getPassword(),registerDto.getUsername());
                userEntity.setPassword(password);
                Boolean result = this.save(userEntity);

                responseData = ResponseData.success(result);
            }else{
                responseData = new ResponseData<>();
                responseData.setCode(2);
                responseData.setMessage("用户名已存在");
            }
        }catch (Exception ex){
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.toString());
        }
        return responseData;
    }
}
