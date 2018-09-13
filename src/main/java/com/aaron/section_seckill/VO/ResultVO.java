package com.aaron.section_seckill.VO;

import lombok.Data;

/**
 * <p>
 * for section_manage
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/9
 */
@Data
public class ResultVO {
    int code;
    String msg;
    Object value;

    private ResultVO(int code, String msg, Object value) {
        this.code = code;
        this.msg = msg;
        this.value = value;
    }

    public static ResultVO success(String msg, Object object) {
        ResultVO resultVO = new ResultVO(200, msg, object);
        return resultVO;
    }

    public static ResultVO failed(String msg) {
        ResultVO resultVO = new ResultVO(400, msg, null);
        return resultVO;
    }

    public static ResultVO failed(String msg, Object value) {
        ResultVO resultVO = new ResultVO(400, msg, value);
        return resultVO;
    }
}
