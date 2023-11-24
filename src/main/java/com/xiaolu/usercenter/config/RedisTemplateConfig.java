package com.xiaolu.usercenter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/11/21 8:52
 * @Description 自定义 redis 序列化器
 */
@Configuration
public class RedisTemplateConfig {

    @Bean
    @ConditionalOnSingleCandidate
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        // 指定序列化器
        redisTemplate.setKeySerializer(RedisSerializer.string());
        return  redisTemplate;

    }
}
