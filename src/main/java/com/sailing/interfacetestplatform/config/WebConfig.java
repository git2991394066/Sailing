package com.sailing.interfacetestplatform.config;

import com.sailing.interfacetestplatform.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 23:48:37
 * @description:全局MVC特性配置
 **/
@Configuration
public class WebConfig {
    @Autowired
    AuthenticationInterceptor authenticationInterceptor;

    /**
     * 添加拦截器和过滤规则，并排除部分swagger-ui的静态资源
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-resources/**", "/v2/**", "/swagger-ui.html/**");
    }

    /**
     * 配置允许跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)     //允许跨越发送cookie
                .allowedOriginPatterns("*")  //允许所有域名进行跨域调用
                .allowedHeaders("*")    //放行全部原始头信息
                .allowedMethods("*");   //允许所有请求方法跨域调用
    }
}
