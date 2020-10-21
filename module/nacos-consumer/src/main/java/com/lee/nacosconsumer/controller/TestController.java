package com.lee.nacosconsumer.controller;

import com.lee.nacosconsumer.client.NacosProviderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestController {
    @Autowired
    private NacosProviderClient providerClient;

    @GetMapping(value = "/echo/{str}")
    public String testabc(@PathVariable String str){
        return providerClient.getEcho(str);
    }


    /************************************ 测试 naocs 配置中心 ************************************/

    @Value("${zhang:zzz}")
    private String zhang;

    @GetMapping("/getConfig")
    public String getNacosConfig() {
        return zhang;
    }

    @Value("${nacos.share}")
    private String share;


//    @Value("${share.config1}")
//    private String shareConfig1;

    @Value("${share.config0}")
    private String shareConfig2;

    @RequestMapping("/getValue")
    public String getValue() {
        return share;
    }

//    @RequestMapping("/getShare1")
//    public String getShare1() {
//        return shareConfig1;
//    }

    @RequestMapping("/getShare2")
    public String getShare2() {
        return shareConfig2;
    }


//    @Value("${share.config3}")
//    private String shareConfig3;

    @Value("${share.config}")
    private String shareConfig4;

//    @RequestMapping("/getShare3")
//    public String getShare3() {
//        return shareConfig3;
//    }

    @RequestMapping("/getShare4")
    public String getShare4() {
        return shareConfig4;
    }

}