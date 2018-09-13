package com.aaron.section_seckill.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * for section_seckill
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/10
 */
@Slf4j
@Component
public class RedisDao {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public boolean SET(String storePrefix, String key, Object object, Long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(storePrefix + ":" + key , object, time, timeUnit);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean SET(String storePrefix, String key, Object object) {
        try {
            redisTemplate.opsForValue().set(storePrefix + ":" + key , object);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Object GET(String storePrefix, String key) {
        try {
            Object a = redisTemplate.opsForValue().get(storePrefix + ":" + key);
            return a;
        } catch (Exception ex) {
            log.warn("redis :: no such key named " + storePrefix + ":" + key );
            return null;
        }
    }

    public Long DECR(String storePrefix, String key) {
        String akey = storePrefix + key;
        long a;
        try {
            a = redisTemplate.opsForValue().increment(akey, -1);
            return a;
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean LPUSH(String listName, Object object) {
        try {
            redisTemplate.opsForList().leftPush(listName, object);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean ZADD(String zsetName, Object object, Long value) {
        try {
            redisTemplate.opsForZSet().add(zsetName, object, value);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean ZADD(String zsetName, Object object, Long value, Long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForZSet().add(zsetName, object, value);
            redisTemplate.expire(zsetName, time, timeUnit);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public long ZRANK(String zsetName, Object object) {
        try {
            Long a = redisTemplate.opsForZSet().rank(zsetName, object);
            return a;
        } catch (Exception ex) {
            log.warn("redis ZRANK :: no such object in " + zsetName);
            return -1;
        }
    }

    public boolean ZREM(String zsetName, Object object) {
        try {
            Long a = redisTemplate.opsForZSet().remove(zsetName, object);
            if (a > 0) {
                return true;
            } else
                return false;
        } catch (Exception ex) {
            log.warn("redis ZRANK :: no such object in " + zsetName);
            return false;
        }
    }



}
