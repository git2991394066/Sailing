package com.sailing.interfacetestplatform.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * OkHttp配置
 */
@Configuration
public class OkHttpClientConfig {
    @Value("${ok.http.connect-timeout}")
    private Integer connectTimeout;

    @Value("${ok.http.read-timeout}")
    private Integer readTimeout;

    @Value("${ok.http.write-timeout}")
    private Integer writeTimeout;

    @Value("${ok.http.max-idle-connections}")
    private Integer maxIdleConnections;

    @Value("${ok.http.keep-alive-duration}")
    private Long keepAliveDuration;
    /**
     * OkHttp配置
     * @return
     */
    @Bean
    public OkHttpClient okHttpClient(){

        return new OkHttpClient().newBuilder()
                .connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.SECONDS))   //连接池配置
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> true)
//                .addInterceptor(new LogInterceptor())
                //可以设置代理
//              .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 9999)))
                .build();
    }
}

