package com.lee.nacosconsumer.controller;

import com.lee.nacosconsumer.client.NacosProviderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController



public class TestController {
    @Autowired
    private NacosProviderClient providerClient;

    @GetMapping(value = "/echo/{str}")
    public String testabc(@PathVariable String str){
        return providerClient.getEcho(str);
    }

}
