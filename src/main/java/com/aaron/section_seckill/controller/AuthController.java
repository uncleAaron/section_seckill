package com.aaron.section_seckill.controller;

import com.aaron.section_seckill.VO.ResultVO;
import com.aaron.section_seckill.constant.SessionConstants;
import com.aaron.section_seckill.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * <p>
 * for section_seckill
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/12
 */
@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResultVO login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        boolean ok = authService.checkStudentPassword(username, password);
        if (ok) {
            session.setAttribute(SessionConstants.SESSION_KEY, username);
        }
        return ResultVO.success("登陆成功", "");
    }

    @PostMapping("/logout")
    public ResultVO logout(HttpSession session) {
        String stuid = (String) session.getAttribute(SessionConstants.SESSION_KEY);
        if (StringUtils.isEmpty(stuid))
            return ResultVO.failed("未登录");
        session.removeAttribute(SessionConstants.SESSION_KEY);
        return ResultVO.success("登出成功", "");
    }
}
