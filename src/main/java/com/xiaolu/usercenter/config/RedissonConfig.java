package com.xiaolu.usercenter.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/11/21 11:57
 * @Description Redisson 配置
 */
@Configuration
// 从yml配置文件中注入属性
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    private String host;

    private String port;

    private String password;

    @Bean
    public RedissonClient redissonClient() {
        // 1. 创建配置
        Config config = new Config();
        // use "rediss://" for SSL connection
        String redisAddress = String.format("redis://%s:%s", host, port);
        config.useSingleServer()
                .setAddress(redisAddress)
                .setDatabase(1)
                .setPassword(password);

        // 2. 创建实例
        // Sync and Async API
        RedissonClient redisson = Redisson.create(config);

        return redisson;
    }

}
