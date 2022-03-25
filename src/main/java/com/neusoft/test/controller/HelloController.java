package com.neusoft.test.controller;

import com.neusoft.test.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

/**
 * @Auth lyn
 * @Date 2021/7/15 14:16
 * version 1.0
 */
@Api(value = "/hello",tags = "tags1")
@RestController
public class HelloController {

    @ApiOperation(value = "rest请求",notes = "hello")
    @PostMapping("/hello")
    public String hello(@ApiParam(name="name1",value = "姓名",required = true,example = "liyanan")@RequestBody String name){
        return "hello "+name;
    }

    @ApiOperation(value = "用户请求",notes = "给定一个用户信息，返回两者关系")
    @PostMapping("/love")
    public String love(@ApiParam(name="user",value="传入json格式参数",required = true)@RequestBody User user){
        return "if "+user.getName()+" is love "+user.getLover();
    }
}
