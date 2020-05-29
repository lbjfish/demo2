package com.lee.nacosconsumer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "nacos-provider")
public interface NacosProviderClient {
    @GetMapping(value = "/echo/{string}")
    String getEcho(@PathVariable String string);
}
