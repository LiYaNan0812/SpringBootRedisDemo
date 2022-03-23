package com.neusoft.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auth lyn
 * @Date 2022/3/23 11:15
 * version 1.0
 */
@RestController
public class RedisTestController {

    @Autowired
    private CacheManager cacheManager;

    @Cacheable(value = "indexCodeCache", key = "#id")
    @RequestMapping("/indexcode/{id}")
    public String getIndexCode(@PathVariable(value = "id", required = true) String id) {
        System.out.println("从数据库中进行了查询");
        return "indexcode" + id;
    }

    @GetMapping("/get/redis/cache")
    public String getCache(){
        Cache indexCodeCache = cacheManager.getCache("indexCodeCache");
        System.out.println(indexCodeCache.getName()+":");
        System.out.println(indexCodeCache.get(1,String.class));
        return indexCodeCache.getName()+":"+indexCodeCache.get(1,String.class);
    }
}
