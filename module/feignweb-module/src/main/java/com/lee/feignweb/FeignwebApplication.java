package com.lee.feignweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//启用feign进行远程调用
@EnableFeignClients
//通过@EnableDiscoveryClient向服务中心发现（这样就能调用别的被添加到eureka的服务,如果需要服务间调用，就必加）
//理解错了，事实证明不要这个注解，也能调用其它服务，我的错
@EnableDiscoveryClient
@SpringBootApplication
public class FeignwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignwebApplication.class, args);
    }

}
