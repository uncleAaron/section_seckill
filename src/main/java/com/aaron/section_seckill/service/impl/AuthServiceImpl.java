package com.aaron.section_seckill.service.impl;

import com.aaron.section_seckill.entity.Student;
import com.aaron.section_seckill.mapper.StudentMapper;
import com.aaron.section_seckill.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * for section_seckill
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/12
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    StudentMapper studentMapper;

    @Override
    public boolean checkStudentPassword(String username, String password) {
        Student student = studentMapper.selectById(username);
        if (student != null) {
            return student.getPassword().equals(password);
        }
        return false;
    }
}
