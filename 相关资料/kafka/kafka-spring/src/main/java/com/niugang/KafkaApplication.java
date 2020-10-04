package com.niugang;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;


/**
 * 
 * @ClassName:  KafkaApplication   
 * @Description：启动类  
 * @author: niugang
 * @date:   2018年10月20日 下午7:55:38   
 * @Copyright: 863263957@qq.com. All rights reserved. 
 *
 */
@SpringBootApplication
public class KafkaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(KafkaApplication.class, args);
	}

}
