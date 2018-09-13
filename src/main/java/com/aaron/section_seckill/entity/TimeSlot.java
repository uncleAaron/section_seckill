package com.aaron.section_seckill.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 上课时间信息
 * </p>
 *
 * @author aaron123
 * @since 2018-09-07
 */
@Data
@Accessors(chain = true)
@TableName("time_slot")
public class TimeSlot implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("sec_id")
    private String secId;
    private BigDecimal day;
    @TableField("start_class")
    private BigDecimal startClass;
    @TableField("end_class")
    private BigDecimal endClass;


}
