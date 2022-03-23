package com.neusoft.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auth lyn
 * @Date 2021/7/15 14:16
 * version 1.0
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){
        return "hello spring boot!";
    }
}
