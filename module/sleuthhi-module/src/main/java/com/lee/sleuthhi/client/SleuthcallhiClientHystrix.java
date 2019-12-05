package com.lee.sleuthhi.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SleuthcallhiClientHystrix implements SleuthcallhiClient {

    private static final Logger log = LoggerFactory.getLogger(SleuthcallhiClientHystrix.class);

    @Override
    public String callhi(String name) {
        log.warn("进入断路器-abc。。。");
        return "sorry " + name + "，服务调用好像失败了！！！";
    }
}
