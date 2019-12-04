package com.lee.configclient.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分支 config-actuator-bus 动态刷新解释：
 * 以前的/refresh是每个客户端自己去调用（比如content、feignweb、configclient之类的客户端），少了还好，但是如果客户端特别多，比如1000个加。
 * 那每个都去调用这个地址：（以configclient（port：10027）客户端为例）
 * http://10.0.192.148:10027/config/actuator/refresh。那1000加个客户端都要去调这个1000次那不得累死？
 * 所以，用mq（目前仅支持rabiitmq和kafka），当config-server发现配置更新，所有1000加个客户端都自动更新（观察者呀），这样就很方便了。
 * 使用bus的地址： （看端口号10002，就知道明显是config-server这个服务）
 * http://10.0.192.148:10002/actuator/bus-refresh
 */
@RefreshScope // 使用该注解的类，会在接到SpringCloud配置中心配置刷新的时候，自动将新的配置更新到该类对应的字段中
@RestController
@RequestMapping("config")
@Api(value = "/config", tags = {"测试 config 的controller"})
public class LeeController {
    private static final Logger logger = LoggerFactory.getLogger(LeeController.class);

    @Value("${server.port}")
    String port;

    @Value("${writer}")
    String writer;

    @GetMapping(value = "/hello")
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
    @GetMapping(value = "/hello2")
    public String abc2(){
        return "hello springcloud";
    }

    @ApiOperation(value = "writer的方法")
    @GetMapping(value = "/writer")
    public String writer(){
        return writer;
    }
}
