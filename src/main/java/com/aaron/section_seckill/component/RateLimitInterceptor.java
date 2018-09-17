package com.aaron.section_seckill.component;

import com.aaron.section_seckill.constant.RedisConstants;
import com.aaron.section_seckill.utils.RedisDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 流量控制拦截器
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/12
 */
@Slf4j
@Component
public class RateLimitInterceptor extends HandlerInterceptorAdapter {

    // 是否开启流量控制
    private boolean rateControlFlag = false;
    private Long lastLoadCacheTime = System.currentTimeMillis();
    final static Integer RATE_LIMIT = 5;
    final static long RATE_EXPIRES = 30;  // minute
    @Autowired
    RedisDao redisDao;

    /**
     * 使用redis的过期时间来做访问限制，当用户第一次请求时，注册容量，以后每一次减容量都会刷新过期时间，根据过期时间的距离来补充桶里的而令牌
     * 结合redis的过期时间做的令牌桶
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 暂定使用userkey来作为请求方地址，这里没排除局域网的情况，日后补充
        String userKey = request.getRemoteAddr();

        Integer value = (Integer) redisDao.GET(RedisConstants.RATE_LIMIT, userKey);
        if (value == null) {
            value = RATE_LIMIT;
            redisDao.SET(RedisConstants.RATE_LIMIT, userKey, value, RATE_EXPIRES, TimeUnit.SECONDS);
        } else if (value > 0) {
            Long expiretime = redisDao.getExpire(RedisConstants.RATE_LIMIT, userKey);
            long token = (RATE_EXPIRES - expiretime)/RATE_LIMIT + value;
            if (token > RATE_LIMIT) {
                token = RATE_LIMIT;
            }
            token -= 1;
            redisDao.SET(RedisConstants.RATE_LIMIT, userKey, token, RATE_EXPIRES, TimeUnit.SECONDS);
        } else {
            response.sendError(429, "Rate limit exceeded, wait for the next quarter minute");
            return false;
        }
        return true;
    }
}
