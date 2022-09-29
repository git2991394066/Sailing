package com.sailing.interfacetestplatform.config;

import com.sailing.interfacetestplatform.interceptor.RestTemplateClientHttpRequestInterceptor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * RestTemplate配置
 */
@Configuration
public class RestTemplateConfig {
    @Autowired
    RestTemplateClientHttpRequestInterceptor restTemplateClientHttpRequestInterceptor;
    @Autowired
    OkHttpClient okHttpClient;

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate;// = new RestTemplate();

        //【可选】将底层的HTTP框架由HttpUrlConnection替换成OkHttp3
        ClientHttpRequestFactory factory = httpRequestFactory();
        restTemplate = new RestTemplate(factory);

        //【可选】添加拦截器
        restTemplate.getInterceptors().add(restTemplateClientHttpRequestInterceptor);

        //【可选】配置序列化和支持和类型
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.TEXT_HTML,
                MediaType.TEXT_PLAIN));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);


        return  restTemplate;
    }

    /**
     * 配置OkHttpClient的请求工厂
     * @return
     */
    public ClientHttpRequestFactory httpRequestFactory() {
        return new OkHttp3ClientHttpRequestFactory(okHttpClient);
    }
}
