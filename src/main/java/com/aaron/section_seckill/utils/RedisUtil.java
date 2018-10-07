package com.aaron.section_seckill.utils;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * for section_seckill
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/22
 */
@ConfigurationProperties(prefix = "spring.redis")
public class RedisUtil {

    private static String host;
    private static int port;
    private static int database;

    private static RedisURI redisURI;
    private static RedisClient redisClient;

    static {
        redisURI = RedisURI
                .builder()
                .withHost(host)
                .withPort(port)
                .withDatabase(database)
                .build();
        redisClient = RedisClient.create(redisURI);
    }

    public static RedisCommands<String, String> getCommands() {
        return redisClient.connect().sync();
    }
}
