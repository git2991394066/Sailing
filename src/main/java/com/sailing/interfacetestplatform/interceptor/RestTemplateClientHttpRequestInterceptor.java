package com.sailing.interfacetestplatform.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * RestTemplate拦截器
 */
@Component
@Slf4j
public class RestTemplateClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    //配置的Token
//    @Value("${remoteprj.token}")
//    String token;

    /**
     * 拦截方法
     * @param request
     * @param body
     * @param execution
     * @return
     * @throws IOException
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        log.info("【RestTemplate请求拦截】请求前...");
        //请求添加Token
//        request.getHeaders().add("token",token);
        ClientHttpResponse response = execution.execute(request, body);
        log.info("【RestTemplate请求拦截】请求后...");
        return response;
    }
}
