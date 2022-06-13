package com.sailing.interfacetestplatform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/8/0008 17:11:54
 * @description:将会话托管到redis，在分布式部署时，实现多负载共享会话信息
 **/
@Configuration
//分别设置会话有效时长、会话更新redis模式、命名空间
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds= 120, flushMode = FlushMode.ON_SAVE, redisNamespace = "bug_record")
@EnableRedisHttpSession
public class RedisSessionConfig {
}
