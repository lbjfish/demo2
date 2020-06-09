package com.lee.nacosprovider.controller;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.Service;
import com.alibaba.nacos.client.config.listener.impl.PropertiesListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 所有例子在 https://nacos.io/zh-cn/docs/sdk.html
 */
@RestController
@RefreshScope
@Slf4j
public class DefendController {
    private static Listener listener;

    @PostConstruct
    public void init(){
        listener = new PropertiesListener() {
            @Override
            public void innerReceive(Properties properties) {
                log.info("recieve1={}" + properties);
            }
        };
    }

    /************************************ naocs-运维-Config 部分 ************************************/

    //地址: http://localhost:10031/getNacosConfig?serverAddr=localhost&dataId=shareconfig1.yml&group=DEFAULT_GROUP&content=abcdefg
    @GetMapping("/getNacosConfig")
    public String getNacosConfig(@RequestParam("serverAddr") String serverAddr,
                                 @RequestParam("dataId") String dataId,
                                 @RequestParam("group") String group) throws NacosException {
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        ConfigService configService = NacosFactory.createConfigService(properties);
        String content = configService.getConfig(dataId, group, 5000);
        log.info("发布的内容={}", content);
        return content;
    }

    //地址: http://localhost:10031/getAddListener?serverAddr=localhost&dataId=shareconfig1.yml&group=DEFAULT_GROUP
    @GetMapping("/getAddListener")
    public void getAddListener(@RequestParam("serverAddr") String serverAddr,
                               @RequestParam("dataId") String dataId,
                               @RequestParam("group") String group) throws NacosException {
//        String serverAddr = "localhost";
//        String dataId = "shareconfig1.yml";
//        String group = "DEFAULT_GROUP";
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        ConfigService configService = NacosFactory.createConfigService(properties);
        String content = configService.getConfig(dataId, group, 5000);
        log.info("content={}", content);
        configService.addListener(dataId, group, new PropertiesListener() {

            @Override
            public void innerReceive(Properties properties) {

            }
        });
    }

    //地址: http://localhost:10031/removeListener?serverAddr=localhost&dataId=shareconfig1.yml&group=DEFAULT_GROUP
    @GetMapping("/removeListener")
    public void removeListener(@RequestParam("serverAddr") String serverAddr,
                               @RequestParam("dataId") String dataId,
                               @RequestParam("group") String group) throws NacosException {
//        String serverAddr = "localhost";
//        String dataId = "shareconfig1.yml";
//        String group = "DEFAULT_GROUP";
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        ConfigService configService = NacosFactory.createConfigService(properties);
        configService.removeListener(dataId, group, listener);
    }

    //地址: http://localhost:10031/publishConfig?serverAddr=localhost&dataId=shareconfig1.yml&group=DEFAULT_GROUP&content=abcdefg
    @GetMapping("/publishConfig")
    public boolean publishConfig(@RequestParam("serverAddr") String serverAddr,
                                 @RequestParam("dataId") String dataId,
                                 @RequestParam("group") String group,
                                 @RequestParam("content") String content) throws NacosException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        ConfigService configService = NacosFactory.createConfigService(properties);
        boolean isPublishOk = configService.publishConfig(dataId, group, content);
        log.info("isPublishOk={}",isPublishOk);
        return isPublishOk;
    }

    //地址: http://localhost:10031/removeConfig?serverAddr=localhost&dataId=shareconfig1.yml&group=DEFAULT_GROUP&content=abcdefg
    @GetMapping("/removeConfig")
    public boolean removeConfig(@RequestParam("serverAddr") String serverAddr,
                                @RequestParam("dataId") String dataId,
                                @RequestParam("group") String group) throws NacosException {
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);

        ConfigService configService = NacosFactory.createConfigService(properties);
        boolean isRemoveOk = configService.removeConfig(dataId, group);
        log.info("isRemoveOk={}", isRemoveOk);
        return isRemoveOk;
    }



    /************************************ naocs-运维-Discovery 部分 ************************************/
    //地址: http://localhost:10031/registerInstance?serviceName=nacos.test.3&ip=11.11.11.11&port=8888

    @GetMapping("/registerInstance")
    public void registerInstance(@RequestParam("serviceName") String serviceName,
                                @RequestParam("ip") String ip,
                                @RequestParam("port") int port) throws NacosException {
        NamingService naming = NamingFactory.createNamingService("localhost");
        naming.registerInstance(serviceName, ip, port);
    }

    @GetMapping("/registerInstance2")
    public void registerInstance2(@RequestParam("serviceName") String serviceName,
                                 @RequestParam("ip") String ip,
                                 @RequestParam("port") int port) throws NacosException {
        Instance instance = new Instance();
        instance.setIp(ip);
        instance.setPort(port);
        instance.setHealthy(false);
        instance.setWeight(2.0);
        Map<String, String> instanceMeta = new HashMap<>();
        instanceMeta.put("site", "et2");
        instance.setMetadata(instanceMeta);
        NamingService naming = NamingFactory.createNamingService("localhost");
        naming.registerInstance(serviceName, instance);
    }

    @GetMapping("/getAllInstances")
    public List<Instance> getAllInstances(@RequestParam("serviceName") String serviceName) throws NacosException {
        NamingService naming = NamingFactory.createNamingService("localhost");
        List<Instance> list = naming.getAllInstances(serviceName);
        return list;
    }

    @GetMapping("/selectInstances")
    public List<Instance> selectInstances(@RequestParam("serviceName") String serviceName, @RequestParam("healthy") boolean healthy) throws NacosException {
        NamingService naming = NamingFactory.createNamingService("localhost");
        List<Instance> list = naming.selectInstances(serviceName, healthy);
        return list;
    }

    @GetMapping("/subscribe")
    public void subscribe(@RequestParam("serviceName") String serviceName) throws NacosException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, "localhost");
        NamingService naming = NamingFactory.createNamingService(properties);
        //这个不是lambda，下面那个是lambda，效果一样
//        naming.subscribe(serviceName, new EventListener() {
//            @Override
//            public void onEvent(Event event) {
//                if(event instanceof NamingEvent){
//                    List<Instance> instances =  ((NamingEvent)event).getInstances();
//                    String serviceName = ((NamingEvent)event).getServiceName();
//                    if(instances.size() == 0) {
//                        //模拟发邮件操作
//                        //SendMailUtil.sendMail(serviceName+"服务下线","服务异常下线赶紧去看看");
//                        log.info("============服务下线"+serviceName);
//                        log.info("============"+instances);
//                    }else {
//
//                        log.info("============服务上线"+serviceName);
//                        log.info("============"+instances);
//                    }
//                }
//            }
//        });

        //上面不是lambda，这个是lambda
        naming.subscribe(serviceName, event -> {
            if(event instanceof NamingEvent){
                List<Instance> instances =  ((NamingEvent)event).getInstances();
                String serviceName1 = ((NamingEvent)event).getServiceName();
                if(instances.size() == 0) {
                    //模拟发邮件操作
                    //SendMailUtil.sendMail(serviceName+"服务下线","服务异常下线赶紧去看看");
                    log.info("============服务下线"+ serviceName1);
                    log.info("============"+instances);
                }else {

                    log.info("============服务上线"+ serviceName1);
                    log.info("============"+instances);
                }
            }
        });
    }

    @GetMapping("/unsubscribe")
    public void unsubscribe(@RequestParam("serviceName") String serviceName) throws NacosException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, "localhost");
        NamingService naming = NamingFactory.createNamingService(properties);
        //这个是lambda
        naming.unsubscribe(serviceName, event -> {
            if(event instanceof NamingEvent){
                List<Instance> instances =  ((NamingEvent)event).getInstances();
                String serviceName1 = ((NamingEvent)event).getServiceName();
                if(instances.size() == 0) {
                    //模拟发邮件操作
                    //SendMailUtil.sendMail(serviceName+"服务下线","服务异常下线赶紧去看看");
                    log.info("============服务下线"+ serviceName1);
                    log.info("============"+instances);
                }else {

                    log.info("============服务上线"+ serviceName1);
                    log.info("============"+instances);
                }
            }
        });
    }
}
