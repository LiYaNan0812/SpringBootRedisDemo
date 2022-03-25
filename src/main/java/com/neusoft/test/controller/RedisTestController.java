package com.neusoft.test.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

/**
 * @Auth lyn
 * @Date 2022/3/23 11:15
 * version 1.0
 */
@Api(value = "/indexcode",tags = {"tags1","tags2"})
@RestController
public class RedisTestController {

    @Autowired
    private CacheManager cacheManager;

    @ApiOperation(value = "测试缓存",notes = "getIndexCode说明")
    @ApiImplicitParam(name = "id",value = "缓存的id",required = true,paramType = "path")
    @Cacheable(value = "indexCodeCache", key = "#id")
    @RequestMapping(value="/indexcode/{id}",method = RequestMethod.GET)
    public String getIndexCode(@PathVariable(value = "id", required = true) String id) {
        System.out.println("从数据库中进行了查询");
        return "indexcode" + id;
    }

    @ApiOperation(value = "从缓存中获取")
    @PostMapping("/get/redis/cache")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idx",value = "缓存的key",paramType = "query",required = true,dataType = "String")
    })
    public String getCache(@RequestParam(value = "idx") Integer id){
        Cache indexCodeCache = cacheManager.getCache("indexCodeCache");
        System.out.println(indexCodeCache.getName()+":");
        System.out.println(indexCodeCache.get(id,String.class));
        return indexCodeCache.getName()+":"+indexCodeCache.get(id,String.class);
    }
}
