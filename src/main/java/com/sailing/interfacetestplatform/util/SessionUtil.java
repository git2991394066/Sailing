package com.sailing.interfacetestplatform.util;

import com.sailing.interfacetestplatform.entity.UserEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/8/0008 18:09:53
 * @description:会话工具，用于设置和获取登录用户信息
 **/
@Component
public class SessionUtil {

    @Autowired
    HttpSession httpSession;

    final String CURRENT_USER_KEY="currentUser";
    /**
     * 设置当前用户
     * @param currentUser
     */
    public void setCurrentUser(CurrentUser currentUser){
        httpSession.setAttribute(CURRENT_USER_KEY,currentUser);
    }
    /**
     * 获取当前用户
     * @return
     */
    public CurrentUser getCurrentUser(){
        CurrentUser currentUser=null;
        Object result=httpSession.getAttribute(CURRENT_USER_KEY);
        if(result!=null){
            currentUser=(CurrentUser) result;
        }
        return currentUser;
    }

    @Data
    public static class CurrentUser implements Serializable{
        private UserEntity userEntity;
    }

}
