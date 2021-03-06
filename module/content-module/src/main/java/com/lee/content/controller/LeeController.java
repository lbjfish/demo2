package com.lee.content.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("test2")
@Api(value = "/test2", tags = {"复制上一个控制层，完全一样"})
public class LeeController {
    private static final Logger logger = LoggerFactory.getLogger(LeeController.class);

    @Value("${server.port}")
    String port;

    @GetMapping(value = "/hello")
    @ApiOperation(value = "传入参数查看结果方法", notes = "根据url传来的name更新信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "传过来的名字", required = true, dataType = "string"),
            @ApiImplicitParam(name = "age" , value = "传过来的年龄")
    })
    public String abc(@RequestParam(value = "name") String name, int age){
        logger.info("name={}",name);
        logger.info("age={}",age);
        return "hello springcloud test " + name + ", my age is " + age;
    }

    @ApiOperation(value = "不带参数的方法")
    @GetMapping(value = "/hello2")
    public String abc2(){
        return "hello springcloud";
    }

    @GetMapping(value = "/getRelMapData")
    @ApiOperation(value = "返回值是map类型的方法")
    @ApiImplicitParam(name = "name", value = "你的名字", required = true, defaultValue = "lee")
    public Map<String,Object> getRelMapData(@RequestParam(value = "name") String name){
        Map<String,Object> map = new HashMap<>();
        map.put("nameOne","zhangsan");
        map.put("nameTwo","lisi");
        map.put("nameParam",name);
        return map;
    }

    @PostMapping(value = "/getParamMap")
    @ApiOperation(value = "参数是map类型的方法")
    @ApiImplicitParam(value = "传来的map对象", required = true)
    public Map<String,Object> getParamMap(@RequestBody Map<String,Object> map){
        Map<String,Object> map1 = map;
        return map1;
    }

    @PostMapping(value = "/getListParamMap")
    @ApiOperation(value = "参数是list类型的方法")
    @ApiImplicitParam(value = "传来的list对象", required = true)
    public List<String> getListParamMap(@RequestBody List<String> list){
        List<String> list2 = list;
        return list;
    }

    @PostMapping(value = "/getParamEntity")
    @ApiOperation(value = "参数是Entity类型的方法")
    @ApiImplicitParam(value = "传来的Entity对象", required = true)
    public MyTemporaryPo getParamEntity(@RequestBody MyTemporaryPo temporaryPo){
        MyTemporaryPo myTemporaryPo = temporaryPo;
        return myTemporaryPo;
    }


    /************************************************ 测试 zuul 用 ***********************************************/
    @GetMapping("/whoami")
    @ApiOperation(value = "zuul测试负载均衡用")
    public String whoami() {
        return "I am from "+ 10122 + ", this is new world";
    }

    /************************************************ 测试 feign 文件下载功能 ***********************************************/
    @GetMapping(value = "/downloadFile")
    @ApiOperation(value = "测试feign 文件下载功能用")
    public void downloadFile(HttpServletResponse response) {
        String filePath = "D://1.txt";
        File file = new File(filePath);
        InputStream in = null;
        if(file.exists()){
            try {
                OutputStream out = response.getOutputStream();
                response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
                response.addHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.ms-excel;charset=UTF-8");
                in = new FileInputStream(file);
                byte buffer[] = new byte[1024];
                int length = 0;
                while ((length = in.read(buffer)) >= 0){
                    out.write(buffer,0,length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(in != null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
