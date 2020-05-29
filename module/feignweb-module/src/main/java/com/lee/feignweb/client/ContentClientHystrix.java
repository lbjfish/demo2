package com.lee.feignweb.client;

import com.lee.feignweb.controller.feign.MyTemporaryPo;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ContentClientHystrix implements ContentClient {

    private static final Logger log = LoggerFactory.getLogger(ContentClientHystrix.class);

    @Override
    public String abc(String name, int age) {
        log.warn("进入断路器-abc。。。");
        return "sorry " + name + "，" + age + "，服务调用好像失败了！！！";
    }

    @Override
    public Map<String, Object> getParamMap(Map<String, Object> map) {
        log.warn("进入断路器-getParamMap。。。");
        throw new RuntimeException("调用getParamMap service失败！！！");
    }

    @Override
    public List<String> getListParamMap(List<String> list) {
        log.warn("进入断路器-getListParamMap。。。");
        throw new RuntimeException("调用 getListParamMap service失败！！！");
    }

    @Override
    public MyTemporaryPo getParamEntity(MyTemporaryPo temporaryPo) {
        log.warn("进入断路器-getParamEntity。。。");
        throw new RuntimeException("getParamEntity service失败！！！");
    }

    @Override
    public String abc2() {
        log.warn("进入断路器-abc2。。。");
        return "sorry，服务调用好像失败了！！！" ;
    }

    @Override
    public String getPort() {
        log.warn("进入断路器-getPort。。。");
        throw new RuntimeException("getPort service失败！！！");
    }

    @Override
    public Response downloadFile() {
        log.warn("进入断路器-downloadFile。。。");
        return null;
    }
}
