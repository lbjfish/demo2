package com.lee.content.controller;

import com.lee.content.lock.common.DistributedLock;
import com.lee.content.utils.RedisUtil;
import com.lee.content.utils.StringRedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 记录下：
 * 可能会报错（java.lang.NumberFormatException: For input string: ""），
 * 没关系，是一些接口参数不是string类型，不影响使用，懒得改了
 */
@RestController
@RequestMapping("redis")
@Api(value = "/redis", tags = {"测试Redis用控制层"})
public class RedisTestController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private StringRedisUtil stringRedisUtil;

    @Autowired
    private DistributedLock distributedLock;

    @GetMapping("/keys")
    public Set<String> keys(String key){
        Set<String> ks = redisTemplate.keys(key);
        return ks;
    }

    @GetMapping("/hasKey")
    public boolean hasKey(String str){
        boolean aa = redisTemplate.hasKey(str);
        return aa;
    }

    @GetMapping("/setString")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "key值", required = true, dataType = "string"),
            @ApiImplicitParam(name = "val" , value = "value值", required = true, dataType = "string")
    })
    public boolean setString(@RequestParam("key") String key, @RequestParam("val") String val){
        redisTemplate.opsForValue().set(key, val);
        return true;
    }

    @GetMapping("/setInteger")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "key值", required = true, dataType = "string"),
            @ApiImplicitParam(name = "val" , value = "value值", required = true, dataType = "int")
    })
    public boolean setInteger(@RequestParam("key") String key, @RequestParam("val") int val){
        redisTemplate.opsForValue().set(key, val);
        return true;
    }

    @GetMapping("/setLong")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "key值", required = true, dataType = "string"),
            @ApiImplicitParam(name = "val" , value = "value值", required = true, dataType = "long")
    })
    public boolean setLong(@RequestParam("key") String key, @RequestParam("val") long val){
        redisTemplate.opsForValue().set(key, val);
        return true;
    }

    @PostMapping("/setObject")
    public boolean setObject(@RequestParam("key") String key, @RequestBody MyTemporaryPo po){
        redisUtil.set(key,po);
        return true;
    }

    @PostMapping("/setListObject")
    public boolean setListObject(@RequestParam("key") String key, @RequestBody List<MyTemporaryPo> po){
        redisUtil.set(key,po);
        return true;
    }

    @GetMapping("/get")
    public Object get(String str){
        Object abcd = redisTemplate.opsForValue().get(str);
        return abcd;
    }

    @GetMapping("/getPlus")
    public Object getPlus(String key){
        Long abcd = getObject(key, Long.class);
        return abcd;
    }

    public  <T> T getObject(String key, Class<T> clazz) {
        Object valueObj = redisTemplate.opsForValue().get(key);
        if (clazz.isInstance(valueObj)) {
            return (T) valueObj;
        } else if (clazz == Long.class && valueObj instanceof Integer) {
            Integer obj = (Integer) valueObj;
            return (T) Long.valueOf(obj.longValue());
        }
        return null;
    }

    @PostMapping("/hmset")
    public boolean hmset(String key, @RequestBody Map<String, Object> map){
        redisUtil.hmset(key, map);
        return true;
    }

    @GetMapping("/hset")
    public boolean hset(String key, String item, Integer value){
        redisTemplate.opsForHash().put(key, item, value);
        return true;
    }

    @GetMapping("/hdel")
    public boolean hdel(String key, String item){
        redisTemplate.opsForHash().delete(key, item);
        return true;
    }

    @GetMapping("/hget")
    public Object hget(String key, String item){
        Object obj = redisTemplate.opsForHash().get(key, item);
        return obj;
    }

    @GetMapping("/hmget")
    public Map hmget(String key){
        Map<Object, Object> map = redisUtil.hmget(key);
        return map;
    }

    @PostMapping("/lSet")
    public boolean lSet(@RequestParam("key") String key, @RequestBody List<MyTemporaryPo> list){
        return redisUtil.lSet(key, list);
    }

    @GetMapping("/lGet")
    public List<Object> lGet(String key, long start, long end){
        List<Object> list = redisUtil.lGet(key, start, end);
        return list;
    }

    @GetMapping("/lGetSize")
    public long lGetSize(String key){
        Long ls = redisUtil.lGetListSize(key);
        return ls;
    }

    @GetMapping("/lGetIndex")
    public Object lGetIndex(String key, long index){
        Object ls = redisUtil.lGetIndex(key, index);
        return ls;
    }

    @GetMapping("/lRemove")
    public Object lRemove(String key, long count, String value){
        Object ls = redisUtil.lRemove(key, count, value);
        return ls;
    }
}
