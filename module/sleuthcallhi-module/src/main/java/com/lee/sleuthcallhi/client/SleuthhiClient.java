package com.lee.sleuthcallhi.client;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "sleuthhi-module-lbj", fallback = SleuthhiClientHystrix.class)
public interface SleuthhiClient {

    @GetMapping(value = "/hi")
    @ApiOperation(value = "传入参数查看结果方法", notes = "根据url传来的name更新信息")
    @ApiImplicitParam(name = "name", value = "传过来的名字", required = true, dataType = "string")
    String hi(@RequestParam(value = "name") String name);
}
