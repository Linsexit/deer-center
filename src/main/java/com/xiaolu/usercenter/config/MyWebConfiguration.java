package com.xiaolu.usercenter.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/11/12 23:27
 * @Description
 */
// @SpringBootConfiguration
class MyWebConfigurer implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        /**
         * 所有请求都允许跨域，使用这种配置就不需要
         * 在interceptor中配置header了
         */
        corsRegistry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("http://127.0.0.1:5173", "http://localhost:8000")
                // .allowedOriginPatterns("*")
                // 设置允许的方法
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                // 是否允许证书
                .allowCredentials(true)
                // 跨域允许时间
                .maxAge(3600)
                .allowedHeaders("*");
    }

}
