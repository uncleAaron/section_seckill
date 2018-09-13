package com.aaron.section_seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRabbit
@MapperScan("com.aaron.section_seckill.mapper")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds= 3600, redisNamespace = "sessions")
@EnableCaching
public class SectionSeckillApplication {

	public static void main(String[] args) {
		SpringApplication.run(SectionSeckillApplication.class, args);
	}
}
