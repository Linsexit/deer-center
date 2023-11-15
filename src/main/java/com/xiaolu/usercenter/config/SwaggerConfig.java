package com.xiaolu.usercenter.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 林小鹿
 * @desc swagger配置类，访问地址为 项目地址 / doc.html
 */
@Configuration
// 开启 Swagger和 Knife4j
@EnableSwagger2
@EnableKnife4j
// 指定该bean只在dev环境生效
@Profile({"dev", "test"})
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //是否开启 (true 开启  false隐藏。生产环境建议隐藏)
                //.enable(false)
                .select()
                //扫描的路径包,设置basePackage会将包下的所有被@Api标记类的所有方法作为api
                .apis(RequestHandlerSelectors.basePackage("com.xiaolu.usercenter.controller"))
                //指定路径处理PathSelectors.any()代表所有的路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //设置文档标题(API名称)
                .title("惠康宝用户中心")
                //文档描述
                .description("惠康宝用户中心接口文档")
                //服务条款URL
                .termsOfServiceUrl("http://center.deerlonger.top")
                // 联系方式
                .contact(new Contact("xiaolu", "https://blog.csdn.net/weixin_62584200", "1638525640@qq.com"))
                //版本号
                .version("1.0.0")
                .build();
    }
}
