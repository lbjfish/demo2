package com.lee.sleuthcallhi.controller.feign;

import brave.Tracer;
import com.lee.sleuthcallhi.client.SleuthhiClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("callhi")
@Api(value = "/callhi", tags = {"测试feign sleuthcallhi-module调用的controller"})
public class CallingIntefaceController {

    private static final Logger logger = LoggerFactory.getLogger(CallingIntefaceController.class);

    //一般feign调用最好放到service层，不应该放到controller层，但是为了方便，所有暂时放到这里
    @Autowired
    private SleuthhiClient sleuthhiClient;

    //在页面上可以查看每个请求的traceId，每个trace又包含若干的span，每个span又包含了很多的tag，自定义tag可以通过Tracer这个类来自定义
    //页面根据Annotation查询，例如（name=libaojun）,对应下面写的
    @Autowired
    private Tracer tracer;

    @Value("${server.port}")
    String port;

    @GetMapping(value = "/feignHi")
    @ApiOperation(value = "feign传入参数查看结果方法", notes = "根据url传来的name更新信息")
    @ApiImplicitParam(name = "name", value = "传过来的名字", required = true, dataType = "string")
    public String testFeignCalling(@RequestParam("name") String name){
        tracer.currentSpan().tag("name","libaojun");
        logger.info("name={}",name);
        String returnVal = sleuthhiClient.hi(name);
        return "我的端口号是：" + port + "。我调用的服务是：" + returnVal;
    }
}
