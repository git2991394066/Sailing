package com.sailing.interfacetestplatform.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sailing.interfacetestplatform.dto.output.account.LoginOutputDto;
import com.sailing.interfacetestplatform.util.JWTUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 23:51:23
 * @description:拦截创建人、创建时间、更新人、更新时间工具类
 **/
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {
    //使用会话
//    @Autowired
//    SessionUtil sessionUtil;
//
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        //获取当前用户，使用会话
//        SessionUtil.CurrentUser currentUser = this.getCurrentUser();
//
//        //拦截设置创建人和更新人，实际项目中可来自会话、token中的登录用户
//        this.setFieldValByName("createById", currentUser.getUserEntity().getId(), metaObject);
//        this.setFieldValByName("createByName", currentUser.getUserEntity().getName(), metaObject);
//        this.setFieldValByName("updateById", currentUser.getUserEntity().getId(), metaObject);
//        this.setFieldValByName("updateByName", currentUser.getUserEntity().getName(), metaObject);
//        //拦截设置创建时间和更新时间
//        Date current = new Date();
//        this.setFieldValByName("createTime", current, metaObject);
//        this.setFieldValByName("updateTime", current, metaObject);
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        //获取当前用户，使用会话
//        SessionUtil.CurrentUser currentUser = this.getCurrentUser();
//
//        //拦截设置更新人，实际项目中可来自会话、token中的登录用户
//        this.setFieldValByName("updateById", currentUser.getUserEntity().getId(), metaObject);
//        this.setFieldValByName("updateByName", currentUser.getUserEntity().getName(), metaObject);
//        //拦截设置更新时间
//        this.setFieldValByName("updateTime", new Date(), metaObject);
//    }
//    /**
//     * 获取当前用户,获取不到就用匿名用户，使用会话形式
//     * @return
//     */
//    public SessionUtil.CurrentUser getCurrentUser(){
//        SessionUtil.CurrentUser currentUser  =  sessionUtil.getCurrentUser();
//        if(currentUser == null){
//            currentUser = new SessionUtil.CurrentUser();
//
//            UserEntity userEntity = new UserEntity();
//            userEntity.setId(-1);
//            userEntity.setUsername("anonymous");
//            userEntity.setName("匿名用户");
//            currentUser.setUserEntity(userEntity);
//        }
//
//        return currentUser;
//    }
    //使用使用JWT Token形式
    @Override
    public void insertFill(MetaObject metaObject) {
        //获取当前用户
        LoginOutputDto currentUser = this.getCurrentUser();

        //拦截设置创建人和更新人，实际项目中可来自会话、token中的登录用户
        this.setFieldValByName("createById", currentUser.getId(), metaObject);
        this.setFieldValByName("createByName", currentUser.getName(), metaObject);
        this.setFieldValByName("updateById", currentUser.getId(), metaObject);
        this.setFieldValByName("updateByName", currentUser.getName(), metaObject);
        //拦截设置创建时间和更新时间
        Date current = new Date();
        this.setFieldValByName("createTime", current, metaObject);
        this.setFieldValByName("updateTime", current, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //获取当前用户
        LoginOutputDto currentUser = this.getCurrentUser();

        //拦截设置更新人，实际项目中可来自会话、token中的登录用户
        this.setFieldValByName("updateById", currentUser.getId(), metaObject);
        this.setFieldValByName("updateByName", currentUser.getName(), metaObject);
        //拦截设置更新时间
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
    /**
     * 获取当前用户，使用JWT Token形式
     * @return
     */
    public LoginOutputDto getCurrentUser(){
        //获取token
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String token = attr.getRequest().getHeader("Authorization");
        LoginOutputDto loginOutputDto = JWTUtil.getLoginData(token);
        if(loginOutputDto == null){
            loginOutputDto = new LoginOutputDto();

            loginOutputDto.setId(-1);
            loginOutputDto.setUsername("anonymous");
            loginOutputDto.setName("匿名用户");
        }

        return loginOutputDto;
    }
}
