package com.aaron.section_seckill.service;

/**
 * <p>
 * for section_seckill
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/12
 */
public interface AuthService {

    boolean checkStudentPassword(String username, String password);
}
