package com.niugang.config;

import org.springframework.kafka.annotation.KafkaListener;

/**
 * 
 * @ClassName:  ConsumerListener   
 * @Description:消费者监听  
 * @author: niugang
 * @date:   2018年10月21日 下午2:05:21   
 * @Copyright: 863263957@qq.com. All rights reserved. 
 *
 */
public class ConsumerListener {
    /**
     * topicPattern:支持正则表达式
     * @param foo
     */
	@KafkaListener(id = "foo", topics = "annotated1")
	public void listen1(String foo) {
		System.out.println("接收消息为："+foo);
	}
}