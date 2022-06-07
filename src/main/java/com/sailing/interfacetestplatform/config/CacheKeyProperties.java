package com.sailing.interfacetestplatform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 23:42:11
 * @description:获取配置属性
 **/
@ConfigurationProperties(prefix = "mycache")
@Component
public class CacheKeyProperties {
    private final Map<String, Duration> cacheConfigs = new HashMap<>();

    public Map<String, Duration> getCacheConfigs() {
        Map<String, Duration> result =  cacheConfigs;
        return result;
    }

    /*
    Duration,jdk1.8引入，一般用于计算时间差
    Duration字符串类似数字有正负之分,默认正,负以’-‘开头,紧接着’P’,下面所有字母都不区分大小写:
    ‘D’ – 天
    ‘H’ – 小时
    ‘M’ – 分钟
    ‘S’ – 秒
    字符’T’是紧跟在时分秒之前的，每个单位都必须由数字开始,且时分秒顺序不能乱,比如:P2DT3M5S,P3D,PT3S
     */
}
