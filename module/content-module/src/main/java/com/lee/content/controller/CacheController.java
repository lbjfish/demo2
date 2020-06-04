package com.lee.content.controller;

import com.lee.content.entity.UserDO;
import com.lee.content.service.UserService;
import com.lee.content.service.impl.CustomizeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cache")
@Api(value = "/cache", tags = {"测试cache用控制层"})
public class CacheController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomizeService customizeService;

    @GetMapping("/findOne")
    public UserDO findOne(Long id){
        UserDO ud = userService.findById(id);
        return ud;
    }

    @GetMapping("/findOneByName")
    public UserDO findOneByName(String name){
        UserDO ud = userService.getByUsername(name);
        return ud;
    }

    @GetMapping("/findAll")
    public Iterable<UserDO> findAll(){
        Iterable<UserDO> uds = userService.findAll();
        return uds;
    }

    @PostMapping("/save")
    public UserDO save(@RequestBody UserDO userDO){
        UserDO uds = userService.save(userDO);
        return uds;
    }

    @PostMapping("/del")
    public boolean del(Long id){
        userService.delteById(id);
        return true;
    }

    @PostMapping("/delAll")
    public boolean delAll(){
        userService.deleteAll();
        return true;
    }




    /********************************************** 自定义分类（CustomizeService） **************************************************/

    @GetMapping("/findOneCus")
    public MyTemporaryPo findOneCus(int id){
        MyTemporaryPo ud = customizeService.findById(id);
        return ud;
    }

    @GetMapping("/findAllCus")
    public List<MyTemporaryPo> findAllCus(){
        List<MyTemporaryPo> uds = customizeService.findAll();
        return uds;
    }

    @PostMapping("/updateCus")
    public MyTemporaryPo updateCus(@RequestBody MyTemporaryPo po){
        MyTemporaryPo poo = customizeService.update(po);
        return poo;
    }

    @PostMapping("/delCus")
    public boolean delCus(int id){
        customizeService.delteById(id);
        return true;
    }
}
