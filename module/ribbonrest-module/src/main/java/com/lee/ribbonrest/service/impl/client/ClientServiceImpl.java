package com.lee.ribbonrest.service.impl.client;

import com.lee.ribbonrest.controller.MyTemporaryPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClientServiceImpl {

    @Autowired
    private RestTemplate restTemplate;

    public String relStringContentLeeContollerService(String name, Integer age){
        String info = restTemplate.getForObject("http://content-module-lbj/content/test2/hello?name="+name + "&age=" + age, String.class);
        return info;
    }

    public Map<String,Object> relMapContentLeeContollerService(String name){
        Map<String,Object> info = restTemplate.getForObject("http://content-module-lbj/content/test2/getRelMapData?name="+name, HashMap.class);
        return info;
    }

    /**
     * 因为参数是Map或者别的自定义的Entity类型，所以必须这么调用
     * @param map
     * @return
     */
    public Map<String,Object> paramMapContentLeeContollerService(Map<String,Object> map){
        //Map<String,Object> info = restTemplate.getForObject("http://content-module-lbj/content/test2/getParamMap", HashMap.class, map);
        Map<String,Object> info = restTemplate.exchange("http://content-module-lbj/content/test2/getParamMap", HttpMethod.POST, new HttpEntity<>(map, new HttpHeaders()), HashMap.class).getBody();
        return info;
    }

    /**
     * 因为参数是Map或者别的自定义的Entity类型，所以必须这么调用
     * @param map
     * @return
     */
    public MyTemporaryPo paramEntityContentLeeContollerService(MyTemporaryPo temporaryPo){
        //Map<String,Object> info = restTemplate.getForObject("http://content-module-lbj/content/test2/getParamMap", HashMap.class, map);
        MyTemporaryPo info = restTemplate.exchange("http://content-module-lbj/content/test2/getParamEntity", HttpMethod.POST, new HttpEntity<>(temporaryPo, new HttpHeaders()), MyTemporaryPo.class).getBody();
        return info;
    }
}
