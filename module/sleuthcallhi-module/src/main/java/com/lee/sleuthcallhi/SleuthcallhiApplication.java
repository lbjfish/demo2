package com.lee.sleuthcallhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//启用feign进行远程调用
@EnableFeignClients
//EnableEurekaClient表明为EurekaClient
@EnableEurekaClient
@SpringBootApplication
public class SleuthcallhiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SleuthcallhiApplication.class, args);
    }

}
