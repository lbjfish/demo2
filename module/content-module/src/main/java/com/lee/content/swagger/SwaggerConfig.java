package com.lee.content.swagger;

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

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 作用：
     *      拦截器给swagger指定资源路径（不配置会导致打开swagger显示空白，所有接口都不显示）
     * 当初踩的坑：
     *      当初在不同公司做项目的时候就遇到这个问题，自己新建一个新的mondule-alipay，导致swagger.html打开显示空白，
     *      就是因为没有用拦截器给swaager指定资源路径导致的，当时不知道，现在知道了，记一下。
     * @return
     */
//    @Bean
//    public WebMvcConfigurationSupport addResourceHandlers() {
//        return new WebMvcConfigurationSupport() {
//            @Override
//            public void addResourceHandlers(ResourceHandlerRegistry registry) {
//                registry.addResourceHandler("swagger-ui.html")
//                        .addResourceLocations("classpath:/META-INF/resources/");
//                registry.addResourceHandler("/webjars/**")
//                        .addResourceLocations("classpath:/META-INF/resources/webjars/");
//            }
//        };
//    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                //定义分组

                //创建ApiSelectorBuilder对象
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lee.content.controller"))
                //要过滤的接口
                //.paths(Predicates.or(PathSelectors.regex("/api2/.*"))).build()
                //这里不过滤接口，意思所有接口都显示在swagger上
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
                //关闭默认返回值
                //.useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("活动服务模块API")
                .description("spring boot content module , swagger2 page")
                .contact(new Contact("lbjfish", "https://github.com/lbjfish", "lbjlee@163.com"))
                .version("V1.0")
                .build();
    }
}
