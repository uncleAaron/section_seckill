package com.aaron.section_seckill;

import com.aaron.section_seckill.utils.MQProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SectionSeckillApplicationTests {

	@Autowired
	MQProvider mqProvider;
	@Test
	public void contextLoads() throws InterruptedException {
	}

}
