package com.aaron.section_seckill.controller;

import com.aaron.section_seckill.VO.ResultVO;
import com.aaron.section_seckill.exception.TakeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * for section_manage
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/9
 */
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(TakeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO handle(Exception ex) {
        return ResultVO.failed(ex.toString());
    }
}
