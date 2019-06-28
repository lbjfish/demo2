package com.lee.ribbonrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

//通过@EnableDiscoveryClient向服务中心发现（这样就能调用别的被添加到eureka的服务）
@EnableDiscoveryClient
//EnableEurekaClient表明为EurekaClient
@EnableEurekaClient
@SpringBootApplication
public class RibbonrestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RibbonrestApplication.class, args);
    }


    @Bean
    //LoadBalanced 注解表明restTemplate使用LoadBalancerClient执行请求
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
