package org.niugang.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaSenderTest {

	@Autowired
	private KafkaTemplate<String, String> template;

	@Test
	public void testSimple(){
		for (int i = 0; i < 100; i++) {
			template.send("annotated1", "0", "foo"+i);
		}
	
	}

}
