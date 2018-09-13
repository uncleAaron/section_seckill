package com.aaron.section_seckill.component;

import com.aaron.section_seckill.service.TakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * for section_manage
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/8
 */
@Component
public class TakeInterceptor implements HandlerInterceptor {

    @Autowired
    TakeService takeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean ok = takeService.getTakeStatus();
        if (!ok) {
            response.sendError(403, "选课时间未达，不允许选课");
        }
        return ok;
    }
}
