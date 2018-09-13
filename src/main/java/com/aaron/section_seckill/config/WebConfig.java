package com.aaron.section_seckill.config;

import com.aaron.section_seckill.component.RateLimitInterceptor;
import com.aaron.section_seckill.component.TakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * for section_manage
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/8
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    RateLimitInterceptor rateLimitInterceptor;
    @Autowired
    TakeInterceptor takeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/section/take");
        registry.addInterceptor(takeInterceptor).addPathPatterns("/section/take**");  // 拦截选课请求
    }
}
