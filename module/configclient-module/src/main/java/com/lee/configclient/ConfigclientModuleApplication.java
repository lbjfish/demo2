package com.lee.configclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

//EnableEurekaClient表明为EurekaClient
@EnableEurekaClient
@SpringBootApplication
public class ConfigclientModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigclientModuleApplication.class, args);
    }

}
