package com.aaron.section_seckill.utils.lock;

import com.aaron.section_seckill.constant.RedisConstants;
import com.aaron.section_seckill.utils.RedisUtil;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * <p>
 * for section_seckill
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/22
 */
@Slf4j
public class RedisLock {

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
        RedisCommands<String, String> commands = RedisUtil.getCommands();
        String key = RedisConstants.LOCKED_PREFIX + ":" + bkey;
        if (commands.setnx(key, value)) { // 返回的是boolean
            return true;
        }

        //避免死锁，且只让一个线程拿到锁
        String currentValue = commands.get(key);
        //如果锁过期了
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁的时间
            String oldValues = commands.getset(key, value);

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
        RedisCommands<String, String> commands = RedisUtil.getCommands();
        String key = RedisConstants.LOCKED_PREFIX + ":" + bkey;
        try {
            String currentValue = commands.get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                commands.del(key);
            }
        } catch (Exception e) {
            log.error("『redis分布式锁』解锁异常，{}", e);
        }
    }



}
