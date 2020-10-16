package com.lee.nacosprovider.controller;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.List;

public class Hano {
    private static ConverterAbc<String, List<Integer>> converterAbc = hjk();

    static ConverterAbc<String, List<Integer>> hjk(){
        return lbj -> {
            System.out.println(lbj);
            return Arrays.asList(Integer.parseInt(lbj));
        };
    }

    public static void main(String[] args) {
        List<Integer> list = converterAbc.abc("456");
        System.out.println(list);
    }
}
