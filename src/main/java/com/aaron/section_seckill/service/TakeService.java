package com.aaron.section_seckill.service;

import com.aaron.section_seckill.entity.Takes;
import com.aaron.section_seckill.exception.TakeException;

/**
 * <p>
 * for section_manage
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/7
 */
public interface TakeService {

    public boolean takeSection(Takes takes) throws TakeException;

    /**
     * 选课结束状态，从mysql获取并使用redis暂存选课状态信息，key为switch，value为true false
     * @return
     */
    public boolean getTakeStatus();

    public boolean saveOrSetTakeStatus(boolean status);

    int checkSuccess(String stuid, String secid);
}
