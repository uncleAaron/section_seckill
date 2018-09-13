package com.aaron.section_seckill.constant.enums;

import lombok.Getter;

/**
 * <p>
 * for section_manage
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/8
 */
@Getter
public enum StatusEnum {
    TAKE_FAILED(1, "选课失败");

    int code;
    String msg;

    StatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
