package com.lee.content.service.impl;

import com.lee.content.controller.MyTemporaryPo;
import com.lee.content.entity.UserDO;
import com.lee.content.repository.UserRepository;
import com.lee.content.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class CustomizeService {

    private static List<MyTemporaryPo> list;

    @PostConstruct
    public void init() throws Exception {
        list = new ArrayList<>();
        MyTemporaryPo po = new MyTemporaryPo();
        po.setId(1);
        po.setName("le是多少1");
        po.setAddress("三带眶发的连接块1");
        list.add(po);

        MyTemporaryPo po1 = new MyTemporaryPo();
        po1.setId(2);
        po1.setName("le是多少2");
        po1.setAddress("三带眶发的连接块2");
        list.add(po1);

        MyTemporaryPo po2 = new MyTemporaryPo();
        po2.setId(3);
        po2.setName("le是多少3");
        po2.setAddress("三带眶发的连接块3");
        list.add(po2);
    }

    @Cacheable(value = "MyTemporaryPo", key="'id=' + #id", unless = "#result==null")
    public MyTemporaryPo findById(int id) {
        for(MyTemporaryPo po : list){
            if(Objects.equals(po.getId(), id)){
                return po;
            }
        }
        return null;
    }

    @CachePut(value = "MyTemporaryPo", key = "'id=' + #po.id", condition = "#po.id != null")
    public MyTemporaryPo update(MyTemporaryPo po) {
        for(MyTemporaryPo po1 : list){
            if(Objects.equals(po1.getId(), po.getId())){
                po1.setName(po.getName());
                po1.setAddress(po.getAddress());
                return po1;
            }
        }
        return null;
    }

    @CacheEvict(value = "MyTemporaryPo", key = "'id=' + #id")
    public void delteById(int id) {
        Iterator<MyTemporaryPo> iterator = list.iterator();
        while(iterator.hasNext()){
            MyTemporaryPo po = iterator.next();
            if(Objects.equals(po.getId(), id)){
                iterator.remove();
            }
        }
    }


    @Cacheable(value = "MyTemporaryPo", key="'all'")
    public List<MyTemporaryPo> findAll() {
        return list;
    }

}
