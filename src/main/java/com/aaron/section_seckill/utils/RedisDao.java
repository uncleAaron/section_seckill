package com.aaron.section_seckill.utils;

import com.aaron.section_seckill.constant.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
        String akey = storePrefix + ":" + key;
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

    /*
    redis分布式锁使用的两个命令
    SETNX key value
    GETSET key value
    http://www.redis.cn/commands/setnx.html
     */
    /**
     * 加锁
     * @param bkey   商品id
     * @param value 当前时间+超时时间
     * @return
     */
    public boolean lock(String bkey, String value) {
        String key = RedisConstants.LOCKED_PREFIX + ":" + bkey;
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {     //这个其实就是setnx命令，只不过在java这边稍有变化，返回的是boolean
            return true;
        }

        //避免死锁，且只让一个线程拿到锁
        String currentValue = (String) redisTemplate.opsForValue().get(key);
        //如果锁过期了
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁的时间
            String oldValues = (String) redisTemplate.opsForValue().getAndSet(key, value);

            /*
               只会让一个线程拿到锁
               如果旧的value和currentValue相等，只会有一个线程达成条件，因为第二个线程拿到的oldValue已经和currentValue不一样了
             */
            if (!StringUtils.isEmpty(oldValues) && oldValues.equals(currentValue)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 解锁
     */
    public void unlock(String bkey, String value) {
        String key = RedisConstants.LOCKED_PREFIX + ":" + bkey;
        try {
            String currentValue = (String) redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("『redis分布式锁』解锁异常，{}", e);
        }
    }



}
