package com.sailing.interfacetestplatform.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sailing.interfacetestplatform.annotation.UserRight;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 23:49:29
 * @description:身份验证拦截器
 **/
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    SessionUtil sessionUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //【1】检查拦截类型
        //如果不是RestController注解的Action方法就直接通过
        //当前拦截的信息都在handler上，如果当前handler类型是HandlerMethod，表示是一个Action
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        //获取当前拦截的HandlerMethod
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        //【2】检查是否有接口权限过滤注解
        //获取当前拦截的HandlerMethod方法的@UserRight注解
        UserRight userRight = handlerMethod.getMethodAnnotation(UserRight.class);
        //如果为空，表示方法没有@UserRight注解
        if(userRight == null){
            //获取当前拦截的HandlerMethod方法所在类的@UserRight注解
            Class clazz = handlerMethod.getBean().getClass();
            userRight = (UserRight) clazz.getAnnotation(UserRight.class);
        }
        //如果方法和类上都没有@UserRight注解，不需要鉴权，直接放过
        if(userRight == null){
            return true;
        }

        //【3】认证与授权
        //获取当前登录用户信息
        SessionUtil.CurrentUser currentUser = sessionUtil.getCurrentUser();
        //如果当前没有登录信息，则返回没有权限
        if(currentUser!=null){
            //【3.1】已登录，进行授权验证
            //是否有权限的处理
            //将当前接口需要的角色转换成List
            List<String> needRoles = Arrays.asList(userRight.roles());
            //获取当前用户的角色列表
            List<String> hasRoles = currentUser.getUserEntity().getRoles();
            //当前用户角色为空，或者当前用户角色没有包括当前接口的角色，则授权不通过
            if(hasRoles ==null || hasRoles.stream().filter(item->needRoles.contains(item)).count()<=0){
                ResponseData responseData = new ResponseData();
                responseData.setCode(403);
                responseData.setMessage("拒绝访问，未被授权！");
                String json = new ObjectMapper().writeValueAsString(responseData);
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(json);
                return false;
            }
        }else{
            //【3.2】未登录，返回401
            //未登录，或登录会话过期
            ResponseData responseData = new ResponseData();
            responseData.setCode(401);
            responseData.setMessage("未授权，请登录后再调用！");
            String json = new ObjectMapper().writeValueAsString(responseData);
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(json);
            return false;
        }

        return true;
    }
}
