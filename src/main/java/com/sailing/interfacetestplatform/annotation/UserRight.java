package com.sailing.interfacetestplatform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 23:40:32
 * @description:
 **/
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserRight {
    String[] roles();
}
