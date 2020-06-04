package com.lee.content.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
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

    /********************************************** 版本控制v2.0 **************************************************/

    //可以注入多个doket，也就是多个版本的api，可以在看到有三个版本groupName不能是重复的，v1和v2是ant风格匹配，配置文件
    @Bean
    public Docket api1_0() {
        return new Docket(DocumentationType.SWAGGER_2)
                //定义分组
                .groupName("v1")
                //创建ApiSelectorBuilder对象
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lee.content.controller"))
                //要过滤的接口(用这个做版本控制),不过滤是PathSelectors.any()
                //.paths(Predicates.or(PathSelectors.regex("/api2/.*"))).build()
                //这里不过滤接口，意思所有接口都显示在swagger上
                .paths(PathSelectors.regex("/test/v1.0.*"))
                .build()
                .apiInfo(apiInfo1_0());
        //关闭默认返回值
        //.useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo1_0() {
        return new ApiInfoBuilder()
                .title("活动服务模块API")
                .description("版本1的")
                .contact(new Contact("lbjfish", "https://github.com/lbjfish", "lbjlee@163.com"))
                .version("v1.0")
                .build();
    }

    /********************************************** 版本控制v2.0 **************************************************/

    //可以注入多个doket，也就是多个版本的api，可以在看到有三个版本groupName不能是重复的，v1和v2是ant风格匹配，配置文件
    @Bean
    public Docket api2_0() {
        return new Docket(DocumentationType.SWAGGER_2)
                //定义分组
                .groupName("v2")
                //创建ApiSelectorBuilder对象
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lee.content.controller"))
                //要过滤的接口(用这个做版本控制),不过滤是PathSelectors.any()
                //.paths(Predicates.or(PathSelectors.regex("/api2/.*"))).build()
                //这里不过滤接口，意思所有接口都显示在swagger上
                .paths(PathSelectors.regex("/test/v2.0.*"))
                .build()
                .apiInfo(apiInfo2_0());
        //关闭默认返回值
        //.useDefaultResponseMessages(false);
    }


    private ApiInfo apiInfo2_0() {
        return new ApiInfoBuilder()
                .title("活动服务模块API2")
                .description("版本2的")
                .contact(new Contact("lbjfish", "https://github.com/lbjfish", "lbjlee@163.com"))
                .version("v2.0")
                .build();
    }


    /********************************************** 模块分类 **************************************************/

    //可以注入多个doket，也就是多个版本的api，可以在看到有三个版本groupName不能是重复的，v1和v2是ant风格匹配，配置文件
    @Bean
    public Docket apiOther() {
        return new Docket(DocumentationType.SWAGGER_2)
                //定义分组
                .groupName("LeeGroup")
                //创建ApiSelectorBuilder对象
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lee.content.controller"))
                //要过滤的接口(用这个做版本控制),不过滤是PathSelectors.any()
                //.paths(Predicates.or(PathSelectors.regex("/api2/.*"))).build()
                //这里不过滤接口，意思所有接口都显示在swagger上
                .paths(PathSelectors.regex("/test2.*"))
                .build()
                .apiInfo(apiInfoOther());
        //关闭默认返回值
        //.useDefaultResponseMessages(false);
    }


    private ApiInfo apiInfoOther() {
        return new ApiInfoBuilder()
                .title("Lee Controller模块的")
                .description("Lee Controller模块的")
                .contact(new Contact("lbjfish", "https://github.com/lbjfish", "lbjlee@163.com"))
                .version("LeeController")
                .build();
    }


    @Bean
    public Docket apiOther2() {
        return new Docket(DocumentationType.SWAGGER_2)
                //定义分组
                .groupName("redisGroup")
                //创建ApiSelectorBuilder对象
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lee.content.controller"))
                //要过滤的接口(用这个做版本控制),不过滤是PathSelectors.any()
                //.paths(Predicates.or(PathSelectors.regex("/api2/.*"))).build()
                //这里不过滤接口，意思所有接口都显示在swagger上
                .paths(PathSelectors.regex("/redis.*"))
                .build()
                .apiInfo(apiInfoOther2());
        //关闭默认返回值
        //.useDefaultResponseMessages(false);
    }


    private ApiInfo apiInfoOther2() {
        return new ApiInfoBuilder()
                .title("Lee RedisTestController模块的")
                .description("Lee RedisTestController模块的")
                .contact(new Contact("lbjfish", "https://github.com/lbjfish", "lbjlee@163.com"))
                .version("RedisTestController")
                .build();
    }

    @Bean
    public Docket apiOther3() {
        return new Docket(DocumentationType.SWAGGER_2)
                //定义分组
                .groupName("cacheGroup")
                //创建ApiSelectorBuilder对象
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lee.content.controller"))
                //要过滤的接口(用这个做版本控制),不过滤是PathSelectors.any()
                //.paths(Predicates.or(PathSelectors.regex("/api2/.*"))).build()
                //这里不过滤接口，意思所有接口都显示在swagger上
                .paths(PathSelectors.regex("/cache.*"))
                .build()
                .apiInfo(apiInfoOther3());
        //关闭默认返回值
        //.useDefaultResponseMessages(false);
    }


    private ApiInfo apiInfoOther3() {
        return new ApiInfoBuilder()
                .title("Lee CacheController模块的")
                .description("Lee CacheController模块的")
                .contact(new Contact("lbjfish", "https://github.com/lbjfish", "lbjlee@163.com"))
                .version("CacheController")
                .build();
    }
}
