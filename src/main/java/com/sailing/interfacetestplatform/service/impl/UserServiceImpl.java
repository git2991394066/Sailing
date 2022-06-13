package com.sailing.interfacetestplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.user.UserCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.user.UserResetPasswordInputDto;
import com.sailing.interfacetestplatform.dto.input.user.UserUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.user.UserOutputDto;
import com.sailing.interfacetestplatform.entity.UserEntity;
import com.sailing.interfacetestplatform.mapper.UserMapper;
import com.sailing.interfacetestplatform.service.UserService;
import com.sailing.interfacetestplatform.util.PasswordUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/8/0008 18:37:55
 * @description:
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    @Autowired
    ModelMapper modelMapper;
//    @Autowired
//    SessionUtil sessionUtil;
    //允许配置的角色
    private final String[] ROLES={"admin","staff"};
    @Override
    public ResponseData<List<UserOutputDto>> query(Integer pageIndex, Integer pageSize, String username, String name) {
        ResponseData<List<UserOutputDto>> responseData=new ResponseData<>();
        try{
            QueryWrapper<UserEntity> queryWrapper=new QueryWrapper<>();
            if(username!=null){
                queryWrapper.like("username",username);
            }
            if(name!=null){
                queryWrapper.like("name",name);
            }
            queryWrapper.eq("is_delete",false); //只取没有删除的
            IPage<UserEntity> queryPage=new Page<>(pageIndex,pageSize);
            queryPage=this.page(queryPage,queryWrapper);
            //Entity转DTO
            List<UserOutputDto> outputDtos=queryPage.getRecords().stream().map(s->modelMapper.map(s,UserOutputDto.class)).collect(Collectors.toList());
            responseData.setData(outputDtos);
            responseData.setTotal(queryPage.getTotal());
            responseData.setCode(ResponseData.SUCCESS_CODE);
            responseData.setMessage("查询成功。");
        }catch(Exception exception){
            exception.printStackTrace();
            responseData=ResponseData.failure(exception.toString());
        }
        return responseData;
    }

    @Override
    public ResponseData<List<UserOutputDto>> queryAll() {
        ResponseData<List<UserOutputDto>> responseData;
        try{
            List <UserEntity> entities = this.list(); //this.list() 不带参数就是查询所有
            List<UserOutputDto> outputDtos=entities.stream().filter(s->s.getIsDelete()==false).sorted(Comparator.comparing(UserEntity::getName).reversed()).map(s->modelMapper.map(s,UserOutputDto.class)).collect(Collectors.toList());
            responseData = ResponseData.success(outputDtos);
        }catch(Exception exception){
            exception.printStackTrace();
            responseData = ResponseData.failure(exception.toString());
        }
        return responseData;
    }

    @Override
    public ResponseData<UserOutputDto> getById(Integer id) {
        ResponseData<UserOutputDto> responseData;
        try {
            //验证
            List<String> checkMsgs=new ArrayList<>();
            UserEntity userEntity=super.getById(id);
            if(userEntity == null){
                checkMsgs.add(String.format("ID为[%s]的数据不存在",id));
            }
            if(checkMsgs.size()>0){
                responseData = new ResponseData<>();
                responseData.setCode(1);
                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));
                return responseData;
            }

            //Entity转DTO
            UserOutputDto outputDto = modelMapper.map(userEntity,UserOutputDto.class);

            responseData = ResponseData.success(outputDto);
        }catch (Exception ex){
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.toString());
        }
        return responseData;
    }

    @Override
    public ResponseData<UserOutputDto> create(UserCreateInputDto inputDto) {
        ResponseData<UserOutputDto> responseData;
        try {
            //数据验证
            List<String> checkMsgs = new ArrayList<>();
            //验证用户名是否存在
            QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username",inputDto.getUsername().trim());//用于删除字符串的头尾空白符
            queryWrapper.eq("is_delete",false);
            UserEntity userEntity = this.getOne(queryWrapper,false);
            if(userEntity!=null){
                checkMsgs.add(String.format("用户名[%s]已存在",String.join(",",inputDto.getUsername())));
            }
            //验证角色是否存在
            List<String> nonExistRoles = inputDto.getRoles().stream().filter(s-> Arrays.asList(ROLES).stream().filter(t->t.equalsIgnoreCase(s)).count()<=0).collect(Collectors.toList());
            if(nonExistRoles!=null && nonExistRoles.size()>0){
                checkMsgs.add(String.format("所属角色[%s]不存在",String.join(",",nonExistRoles)));
            }
            if(checkMsgs.size()>0){
                String checkMsg = checkMsgs.stream().collect(Collectors.joining(","));
                responseData = new ResponseData<>();
                responseData.setCode(1);
                responseData.setMessage(checkMsg);

                return responseData;
            }

            //Dto转Entity
            UserEntity entity = modelMapper.map(inputDto,UserEntity.class);

            //密码加密
            String password = PasswordUtil.encrypt(inputDto.getPassword(),inputDto.getUsername());
            entity.setPassword(password);

            entity.setIsDelete(false);
            //新增
            this.save(entity);

            responseData = this.getById(entity.getId());
        }catch (Exception ex){
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.toString());
        }
        return responseData;
    }

    @Override
    public ResponseData<UserOutputDto> update(UserUpdateInputDto inputDto) {
        ResponseData<UserOutputDto> responseData;
        try {
            //数据验证
            List<String> checkMsgs = new ArrayList<>();
            //验证ID是否已经存在
            UserEntity existsEntity = super.getById(inputDto.getId());
            if(existsEntity == null){
                checkMsgs.add(0,String.format("用户ID[%d]不存在",inputDto.getId()));
            }else {
                //验证用户名是否存在
                QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.ne("id", inputDto.getId());
                queryWrapper.eq("username", inputDto.getUsername().trim());
                queryWrapper.eq("is_delete",false);
                UserEntity userEntity = this.getOne(queryWrapper, false);
                if (userEntity != null) {
                    checkMsgs.add(String.format("用户名[%s]已存在", String.join(",", inputDto.getUsername())));
                }
                //验证角色是否存在
                List<String> nonExistRoles = inputDto.getRoles().stream().filter(s->Arrays.asList(ROLES).stream().filter(t->t.equalsIgnoreCase(s)).count()<=0).collect(Collectors.toList());
                if(nonExistRoles!=null && nonExistRoles.size()>0){
                    checkMsgs.add(String.format("所属角色[%s]不存在",String.join(",",nonExistRoles)));
                }
            }
            if(checkMsgs.size()>0){
                String checkMsg = checkMsgs.stream().collect(Collectors.joining(","));
                responseData = new ResponseData<>();
                responseData.setCode(1);
                responseData.setMessage(checkMsg);

                return responseData;
            }

            //Dto转Entity
            UserEntity entity = modelMapper.map(inputDto,UserEntity.class);

            //密码不做修改
            entity.setPassword(existsEntity.getPassword());

            //操作
            this.updateById(entity);

            responseData = this.getById(entity.getId());
        }catch (Exception ex){
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.toString());
        }
        return responseData;
    }

    @Override
    public ResponseData<Boolean> delete(Integer id) {
        ResponseData<Boolean> responseData;
        try {
            //数据验证
            List<String> checkMsgs = new ArrayList<>();
            //验证ID是否已经存在
            UserEntity userEntity = super.getById(id);
            if(userEntity == null){
                checkMsgs.add(0,String.format("用户ID[%d]不存在",id));
            }
            if(checkMsgs.size()>0){
                String checkMsg = checkMsgs.stream().collect(Collectors.joining(","));
                responseData = new ResponseData<>();
                responseData.setCode(1);
                responseData.setMessage(checkMsg);

                return responseData;
            }

            userEntity.setIsDelete(true);

            //删除
            Boolean result =   this.updateById(userEntity);

            responseData = ResponseData.success(result);
        }catch (Exception ex){
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.toString());
        }
        return responseData;
    }

    @Override
    public ResponseData<Boolean> resetPassord(UserResetPasswordInputDto inputDto) {
        ResponseData<Boolean> responseData;
        try {
            //数据验证
            List<String> checkMsgs = new ArrayList<>();
            UserEntity userEntity = this.getById((Serializable)inputDto.getId());

            if(userEntity == null){
                checkMsgs.add(0,String.format("用户ID[%d]不存在",inputDto.getId()));
            }
            if(checkMsgs.size()>0){
                String checkMsg = checkMsgs.stream().collect(Collectors.joining(","));
                responseData = new ResponseData<>();
                responseData.setCode(1);
                responseData.setMessage(checkMsg);

                return responseData;
            }

            //密码加密
            String password = PasswordUtil.encrypt(inputDto.getPassword(),userEntity.getUsername());
            userEntity.setPassword(password);

            //更新
            boolean success = this.updateById(userEntity);

            responseData = ResponseData.success(success);
        }catch (Exception ex){
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.toString());
        }
        return responseData;
    }
}
