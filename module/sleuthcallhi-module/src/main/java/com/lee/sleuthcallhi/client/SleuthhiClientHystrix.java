package com.lee.sleuthcallhi.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SleuthhiClientHystrix implements SleuthhiClient {

    private static final Logger log = LoggerFactory.getLogger(SleuthhiClientHystrix.class);

    @Override
    public String hi(String name) {
        log.warn("进入断路器-abc。。。");
        return "sorry " + name + "，服务调用好像失败了！！！";
    }
}
