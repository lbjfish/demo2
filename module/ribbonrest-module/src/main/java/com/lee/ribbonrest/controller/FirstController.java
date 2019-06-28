package com.lee.ribbonrest.controller;

import com.lee.ribbonrest.service.impl.client.ClientServiceImpl;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("first")
@Api(value = "/first", tags = {"测试ribbon的controller层"})
public class FirstController {
    private static final Logger logger = LoggerFactory.getLogger(FirstController.class);

    @Autowired
    private ClientServiceImpl clientService;

    @GetMapping(value = "/getName")
    @ApiOperation(value = "传入参数name", notes = "根据url传来的name更新信息")
    public Map<String,Object> abc(@RequestParam(value = "name") @ApiParam(value = "姓名", required = true, defaultValue = "God李") String name){
        logger.info("name={}",name);
        Map<String,Object> map = new HashMap<>();
        map.put("nameParam", name);
        map.put("nameDefault","lisi");
        map.put("nameDefault2","wangwu");
        return map;
    }

    @GetMapping(value = "/relString")
    @ApiOperation(value = "这是Ribbon+RestTemplate调用服务接口，返回值String", notes = "根据url传来的name和age更新信息")
    public String paramString(@RequestParam(value = "name", required = true) @ApiParam(value = "姓名") String name, @RequestParam(value = "age") @ApiParam(value = "年龄", defaultValue = "223") String age){
        String info = clientService.relStringContentLeeContollerService(name,Integer.parseInt(age));
        return info;
    }

    @GetMapping(value = "/relMap")
    @ApiOperation(value = "这是Ribbon+RestTemplate调用服务接口，返回值Map", notes = "根据url传来的name和age更新信息")
    public Map<String,Object> relMap(@RequestParam(value = "name", required = true) @ApiParam(value = "姓名", required = true) String name){
        Map<String,Object> info = clientService.relMapContentLeeContollerService(name);
        return info;
    }

    @PostMapping(value = "/paramMap")
    @ApiOperation(value = "这是Ribbon+RestTemplate调用服务接口，参数Map，返回值Map", notes = "根据url传来的name和age更新信息")
    public Map<String,Object> paramMap(@RequestBody Map<String,Object> map){
        Map<String,Object> info = clientService.paramMapContentLeeContollerService(map);
        return info;
    }

    @PostMapping(value = "/paramEntity")
    @ApiOperation(value = "这是Ribbon+RestTemplate调用服务接口，参数Map，返回值Map", notes = "根据url传来的name和age更新信息")
    public MyTemporaryPo paramEntity(@RequestBody MyTemporaryPo myTemporaryPo){
        MyTemporaryPo info = clientService.paramEntityContentLeeContollerService(myTemporaryPo);
        return info;
    }
}
