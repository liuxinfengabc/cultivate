package com.tule.seven.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.tule")
@MapperScan("com.tule.dao")
public class SevenWebApplication {

    public static void main(String[] args){
        SpringApplication.run(SevenWebApplication.class,args);

    }
}
