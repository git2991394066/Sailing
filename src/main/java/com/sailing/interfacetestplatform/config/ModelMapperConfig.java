package com.sailing.interfacetestplatform.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/8/0008 17:05:21
 * @description:对象映射工具Bean
 **/
@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        //添加此配置，让没有主键的转换不会抛出异常
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }

}
