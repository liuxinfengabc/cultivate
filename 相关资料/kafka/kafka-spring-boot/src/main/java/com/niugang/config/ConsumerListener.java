package com.niugang.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


/**
 * 
 * @ClassName: ConsumerListener
 * @Description:消费者监听
 * @author: niugang
 * @date: 2018年10月21日 下午2:05:21
 * @Copyright: 863263957@qq.com. All rights reserved.
 *
 */
@Component
public class ConsumerListener {
	private Logger logger = LoggerFactory.getLogger(ConsumerListener.class);

	@KafkaListener(id = "foo", topics = "kafka-boot")
	public void listen1(String foo) {
		logger.info("message content [{}]", foo);
	}
}