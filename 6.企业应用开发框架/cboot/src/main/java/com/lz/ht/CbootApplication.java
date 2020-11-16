package com.lz.ht;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Administrator
 */
@EnableTransactionManagement
@MapperScan("com.lz.ht.dao")
@SpringBootApplication
public class  CbootApplication {

    public static void main(String[] args) {
        SpringApplication.run( CbootApplication.class, args);
    }

}



