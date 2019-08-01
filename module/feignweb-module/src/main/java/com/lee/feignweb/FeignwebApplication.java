package com.lee.feignweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//启用feign进行远程调用
@EnableFeignClients
//通过@EnableDiscoveryClient向服务中心发现（这样就能调用别的被添加到eureka的服务,如果需要服务间调用，就必加）
@EnableDiscoveryClient
//EnableEurekaClient表明为EurekaClient
@EnableEurekaClient
@SpringBootApplication
public class FeignwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignwebApplication.class, args);
    }

}
