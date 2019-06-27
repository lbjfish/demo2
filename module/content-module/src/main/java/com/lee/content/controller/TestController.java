package com.lee.content.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@Api(value = "/test", tags = {"测试用控制层"})
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping(value = "/v1.0/hello")
    @ApiOperation(value = "传入参数查看结果方法", notes = "根据url传来的name更新信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "传过来的名字", required = true, dataType = "string"),
            @ApiImplicitParam(name = "age" , value = "传过来的年龄")
    })
    public String abc(@RequestParam(value = "name") String name, int age){
        logger.info("name={}",name);
        logger.info("age={}",age);
        return "hello springcloud test " + name + ", my age is " + age;
    }

    @ApiOperation(value = "不带参数的方法")
    @GetMapping(value = "/v2.0/hello2")
    public String abc2(){
        return "hello springcloud";
    }
}
