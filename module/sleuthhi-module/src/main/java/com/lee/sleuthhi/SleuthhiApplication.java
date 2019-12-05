package com.lee.sleuthhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//启用feign进行远程调用
@EnableFeignClients
//EnableEurekaClient表明为EurekaClient
@EnableEurekaClient
@SpringBootApplication
public class SleuthhiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SleuthhiApplication.class, args);
    }

}
