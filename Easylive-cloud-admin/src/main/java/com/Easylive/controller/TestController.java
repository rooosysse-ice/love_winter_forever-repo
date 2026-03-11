package com.Easylive.controller;


import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    private ServletWebServerApplicationContext applicationContext;

    @RequestMapping("/test")
    public String test() {
            return "这是一个微服务web模块admin" + applicationContext.getWebServer().getPort() ;
        }
}
