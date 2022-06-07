package com.sailing.interfacetestplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan("com.sailing.interfacetestplatform.mapper")
public class InterfaceTestPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterfaceTestPlatformApplication.class, args);
    }

}
