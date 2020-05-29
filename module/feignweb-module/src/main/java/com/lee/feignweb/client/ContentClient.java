package com.lee.feignweb.client;

import com.lee.feignweb.controller.feign.MyTemporaryPo;
import feign.Response;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "content-module-lbj", fallback = ContentClientHystrix.class)
public interface ContentClient {

    @GetMapping(value = "/content/test2/hello")
    @ApiOperation(value = "传入参数查看结果方法", notes = "根据url传来的name更新信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "传过来的名字", required = true, dataType = "string"),
            @ApiImplicitParam(name = "age" , value = "传过来的年龄")
    })
    String abc(@RequestParam(value = "name") String name, @RequestParam(value = "age") int age);

    @PostMapping(value = "/content/test2/getParamMap")
    @ApiOperation(value = "参数是map类型的方法")
    @ApiImplicitParam(value = "传来的map对象", required = true)
    Map<String,Object> getParamMap(@RequestBody Map<String,Object> map);

    @PostMapping(value = "/content/test2/getListParamMap")
    @ApiOperation(value = "参数是list类型的方法")
    @ApiImplicitParam(value = "传来的list对象", required = true)
    List<String> getListParamMap(@RequestBody List<String> list);

    @PostMapping(value = "/content/test2/getParamEntity")
    @ApiOperation(value = "参数是Entity类型的方法")
    @ApiImplicitParam(value = "传来的Entity对象", required = true)
    MyTemporaryPo getParamEntity(@RequestBody MyTemporaryPo temporaryPo);

    @ApiOperation(value = "不带参数的方法")
    @GetMapping(value = "/content/test/v2.0/hello2")
    String abc2();

    @ApiOperation(value = "测试eureka做负载均衡")
    @GetMapping("/content/test2/getPort")
    String getPort();

    @GetMapping(value = "/content/test2/downloadFile", consumes = "application/vnd.ms-excel;charset=UTF-8")
    @ApiOperation(value = "测试feign 文件下载功能用")
    Response downloadFile();
}
