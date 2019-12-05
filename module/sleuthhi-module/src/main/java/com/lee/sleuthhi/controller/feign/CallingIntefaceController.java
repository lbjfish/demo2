package com.lee.sleuthhi.controller.feign;

import com.lee.sleuthhi.client.SleuthcallhiClient;
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
@RequestMapping("hi")
@Api(value = "/hi", tags = {"测试feign sleuthhi-module调用的controller"})
public class CallingIntefaceController {

    private static final Logger logger = LoggerFactory.getLogger(CallingIntefaceController.class);

    //一般feign调用最好放到service层，不应该放到controller层，但是为了方便，所有暂时放到这里
    @Autowired
    private SleuthcallhiClient sleuthcallhiClient;

    @Value("${server.port}")
    String port;

    @GetMapping(value = "/feignCallhi")
    @ApiOperation(value = "feign传入参数查看结果方法", notes = "根据url传来的name更新信息")
    @ApiImplicitParam(name = "name", value = "传过来的名字", required = true, dataType = "string")
    public String testFeignCalling(@RequestParam("name") String name){
        logger.info("name={}",name);
        String returnVal = sleuthcallhiClient.callhi(name);
        return "我的端口号是：" + port + "。我调用的服务是：" + returnVal;
    }
}
