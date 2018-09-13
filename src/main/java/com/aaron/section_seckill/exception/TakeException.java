package com.aaron.section_seckill.exception;

import com.aaron.section_seckill.constant.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * for section_manage
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/8
 */
@Getter
@Setter
public class TakeException extends RuntimeException {
    private int code;
    private String msg;
    private String stuid;
    private String secid;
    public TakeException(StatusEnum status, String stuid, String secid) {
        super(status.getMsg());
        msg = status.getMsg();
        code = status.getCode();
        this.secid = secid;
        this.stuid = stuid;
    }

    @Override
    public String toString() {
        return "code: " + code + " " + msg + " stuID: " + stuid + " secID: " + secid;
    }
}
