package com.lee.content.controller;

import java.io.Serializable;

/**
 * 这是临时的实体类，不应该放到这里，应该单独见一个项目（后面有时间再建吧）
 */
public class MyTemporaryPo implements Serializable {
    private int id;
    private String name;
    private String address;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
