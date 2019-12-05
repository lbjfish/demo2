package com.lee.sleuthhi.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiController {

    @Value("${server.port}")
    String port;

    @ApiOperation(value = "参数name的方法")
    @GetMapping(value = "/hi")
    @ApiImplicitParam(name = "name", value = "你的名字", required = true, defaultValue = "lee")
    public String hi(@RequestParam(value = "name") String name){
        return "hi 我是sleuthi-module的接口，我的名字是:" + name + ",我的端口:" + port;
    }
}
