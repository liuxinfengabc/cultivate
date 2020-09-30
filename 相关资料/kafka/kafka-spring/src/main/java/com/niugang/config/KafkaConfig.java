package com.niugang.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;

import com.niugang.controller.SenderConttoller;

/**
 * 
 * @ClassName: KafkaConfig
 * @Description:kafka配置类,基于spring java纯配置的
 * @author: niugang
 * @date: 2018年10月20日 下午8:04:26
 * @Copyright: 863263957@qq.com. All rights reserved.
 *
 */
@Configuration
@EnableKafka
public class KafkaConfig {

	private Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		// 偏移量提交方式
		// factory.getContainerProperties().setAckMode(AbstractMessageListenerContainer.AckMode.COUNT);
		// 异步提交偏移量(默认就是true)
		// factory.getContainerProperties().setSyncCommits(true);
		//回调函数经常用于记录提交错误
	    /*factory.getContainerProperties().setCommitCallback(new OffsetCommitCallback() {

			@Override
			public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
				if (exception != null) {
					logger.error("Commit failed for effsets {}", offsets, exception);
				}

			}
		});*/
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	/**
	 * 消费者工厂配置
	 * 
	 * @return
	 */
	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerProps());
	}

	/**
	 * 生产者工厂配置
	 * 
	 * @return
	 */
	@Bean
	public ProducerFactory<String, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(senderProps());
	}

	/**
	 * kafka发送消息模板
	 * 
	 * @return
	 */
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<String, String>(producerFactory());
	}

	/**
	 * 消费者监听
	 * 
	 * @return
	 */
	@Bean
	public ConsumerListener listener() {
		return new ConsumerListener();
	}

	/**
	 * 消费配置方法
	 * 
	 * @return
	 */
	private Map<String, Object> consumerProps() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka_group_1");
		/**
		 * enable.auto.commit 默认5秒自动提交偏移量
		 */
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		/**
		 * kafka是基于key-value键值对的，以下配置key和value的反序列化放
		 */
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return props;
	}

	/**
	 * 生产者配置方法
	 * 
	 * 生产者有三个必选属性
	 * <p>
	 * 1.bootstrap.servers broker地址清单，清单不要包含所有的broker地址，
	 * 生产者会从给定的broker里查找到其他broker的信息。不过建议至少提供两个broker信息，一旦 其中一个宕机，生产者仍能能够连接到集群上。
	 * </p>
	 * <p>
	 * 2.key.serializer broker希望接收到的消息的键和值都是字节数组。 生产者用对应的类把键对象序列化成字节数组。
	 * </p>
	 * <p>
	 * 3.value.serializer 值得序列化方式
	 * </p>
	 * 
	 * 
	 * @return
	 */
	private Map<String, Object> senderProps() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		/**
		 * 当从broker接收到的是临时可恢复的异常时，生产者会向broker重发消息，但是不能无限
		 * 制重发，如果重发次数达到限制值，生产者将不会重试并返回错误。
		 * 通过retries属性设置。默认情况下生产者会在重试后等待100ms，可以通过 retries.backoff.ms属性进行修改
		 */
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		/**
		 * 在考虑完成请求之前，生产者要求leader收到的确认数量。这可以控制发送记录的持久性。允许以下设置：
		 * <ul>
		 * <li>
		 * <code> acks = 0 </ code>如果设置为零，则生产者将不会等待来自服务器的任何确认。该记录将立即添加到套接字缓冲区并视为已发送。在这种情况下，无法保证服务器已收到记录，并且
		 * <code>retries </ code>配置将不会生效（因为客户端通常不会知道任何故障）。为每条记录返回的偏移量始终设置为-1。
		 * <li> <code> acks = 1 </code>
		 * 这意味着leader会将记录写入其本地日志，但无需等待所有follower的完全确认即可做出回应。在这种情况下，
		 * 如果leader在确认记录后立即失败但在关注者复制之前，则记录将丢失。
		 * <li><code> acks = all </code>
		 * 这意味着leader将等待完整的同步副本集以确认记录。这保证了只要至少一个同步副本仍然存活，记录就不会丢失。这是最强有力的保证。
		 * 这相当于acks = -1设置
		 */
		props.put(ProducerConfig.ACKS_CONFIG, "1");
		/**
		 * 当有多条消息要被发送到统一分区是，生产者会把他们放到统一批里。kafka通过批次的概念来 提高吞吐量，但是也会在增加延迟。
		 */
		// 以下配置当缓存数量达到16kb，就会触发网络请求，发送消息
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		// 每条消息在缓存中的最长时间，如果超过这个时间就会忽略batch.size的限制，由客户端立即将消息发送出去
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		// key的序列化方式
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		// value序列化方式
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return props;
	}
}
