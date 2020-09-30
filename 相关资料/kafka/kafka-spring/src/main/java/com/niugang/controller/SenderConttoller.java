package com.niugang.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SenderConttoller {

	
	@Autowired
	private KafkaTemplate<String, String> template;

    @RequestMapping("sendMessage")
	public String testSimple(){
    	for (int i = 0; i < 100; i++) {
			template.send("annotated1", "0", "foo"+i);
		}
	    return  "success";
	}
}
