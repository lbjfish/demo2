package com.lee.sleuthhi.client;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "sleuthcallhi-module-lbj", fallback = SleuthcallhiClientHystrix.class)
public interface SleuthcallhiClient {

    @ApiOperation(value = "参数name的方法")
    @GetMapping(value = "/callhi")
    @ApiImplicitParam(name = "name", value = "你的名字", required = true, defaultValue = "lee")
    String callhi(@RequestParam(value = "name") String name);
}
