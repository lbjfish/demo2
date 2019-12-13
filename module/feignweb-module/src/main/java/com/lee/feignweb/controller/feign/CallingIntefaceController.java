package com.lee.feignweb.controller.feign;

import com.lee.feignweb.client.ContentClient;
import feign.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("feign")
@Api(value = "/feign", tags = {"测试feign调用的controller"})
public class CallingIntefaceController {

    private static final Logger logger = LoggerFactory.getLogger(CallingIntefaceController.class);

    //一般feign调用最好放到service层，不应该放到controller层，但是为了方便，所有暂时放到这里
    @Autowired
    private ContentClient contentClient;

    @GetMapping(value = "/feignHello")
    @ApiOperation(value = "feign传入参数查看结果方法", notes = "根据url传来的name更新信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "传过来的名字", required = true, dataType = "string"),
            @ApiImplicitParam(name = "age" , value = "传过来的年龄")
    })
    public String testFeignCalling(@RequestParam("name") String name, int age){
        logger.info("name={}",name);
        logger.info("age={}",age);
        String returnVal = contentClient.abc(name, age);
        return "我是feign传过来的接口feignHello：" + returnVal;
    }


    @PostMapping(value = "/feignGetParamMap")
    @ApiOperation(value = "参数是map类型的方法（swagger好像不行,postman可以调通）")
    @ApiImplicitParam(value = "传来的map对象", required = true)
    public Map<String,Object> getParamMap(@RequestBody Map<String,Object> map){
        Map<String,Object> map2 = contentClient.getParamMap(map);
        return map2;
    }

    @PostMapping(value = "/feignGetParamEntity")
    @ApiOperation(value = "参数是Entity类型的方法（swagger好像不行,postman可以调通）")
    @ApiImplicitParam(value = "传来的Entity对象", required = true)
    public MyTemporaryPo getParamEntity(MyTemporaryPo temporaryPo){
        MyTemporaryPo myTemporaryPo = contentClient.getParamEntity(temporaryPo);
        return myTemporaryPo;
    }

    @ApiOperation(value = "feign不带参数的方法（swagger有时候不行,postman可以调通）")
    @GetMapping(value = "/feignHello2")
    public String abc2(){
        String returnVal = contentClient.abc2();
        return "我是feign传过来的接口feignHello2：" + returnVal;
    }

    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public ResponseEntity<byte[]> downFile(){
        ResponseEntity<byte[]> result=null ;
        InputStream inputStream = null;
        try {
            // feign文件下载
            Response response = contentClient.downloadFile();
            Response.Body body = response.body();
            inputStream = body.asInputStream();
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            HttpHeaders heads = new HttpHeaders();
            heads.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=123.txt");
            heads.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            result = new ResponseEntity <byte[]>(b,heads, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
