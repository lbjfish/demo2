package com.lee.gateway;

import com.lee.gateway.filter.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

//通过@EnableDiscoveryClient向服务中心发现（这样就能调用别的被添加到eureka的服务,如果需要服务间调用，就必加）
@EnableDiscoveryClient
//EnableEurekaClient表明为EurekaClient
@EnableEurekaClient
@SpringBootApplication
@RestController
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/get")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri("http://httpbin.org:80"))
                .route(p -> p.host("*.hystrix.com")
                        .filters(f -> f.hystrix(config -> config
                                        .setName("mycmd")
                                        .setFallbackUri("forward://fallback")))
                        .uri("http://httpbin.org:80"))
                .route(p -> p.cookie("name","abcc").uri("http://httpbin.org/delay/3"))
                .build();
    }

    @GetMapping("/fallback/ppp")
    public Mono<String> fallback() {
        return Mono.just("fallback");
    }

    /***************************现在3个Bean可以放到一个类里，带@Configuration的***************************/
    //IP限流
    //@Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    //用户限流
    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
    }

    //接口限流
    //@Bean
    KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }
    /***************************上面3个Bean可以放到一个类里，带@Configuration的***************************/


//    /**
//     * 全局自定义过滤器 （我先关了，要不我访问任何地址都会被拦截）
//     * @return
//     */
//    @Bean
//    public TokenFilter tokenFilter(){
//        return new TokenFilter();
//    }
}
