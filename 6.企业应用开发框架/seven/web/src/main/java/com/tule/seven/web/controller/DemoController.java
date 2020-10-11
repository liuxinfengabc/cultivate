package com.tule.seven.web.controller;

import com.tule.biz.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demo")
public class DemoController {

//    @GetMapping("test")
//    public String test(){
//        return "hello tule";
//    }
    @Autowired
    private DemoService demoService;

    @GetMapping("test")
    public String test(){
        return demoService.test();
    }

    @GetMapping("mybatis")
    public String mybatis(){
        return demoService.mybatis();
    }
}

