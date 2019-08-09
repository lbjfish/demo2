package com.lee.zuul.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2配置
 * 有两种用法：
 *  1.做版本控制
 *  2.做模块分类
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                //定义分组
                //.groupName("v1")
                //创建ApiSelectorBuilder对象
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lee.zuul.controller"))
                //要过滤的接口(用这个做版本控制),不过滤是PathSelectors.any()
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
        //关闭默认返回值
        //.useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("zuul服务模块API")
                .description("测试 zuul 的api文档")
                .contact(new Contact("lbjfish", "https://github.com/lbjfish", "lbjlee@163.com"))
                // .version("v1.0")
                .build();
    }
}
