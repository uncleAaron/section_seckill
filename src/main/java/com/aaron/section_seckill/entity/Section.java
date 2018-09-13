package com.aaron.section_seckill.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author aaron123
 * @since 2018-09-07
 */
@Data
@Accessors(chain = true)
public class Section implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("sec_id")
    private String secId;
    @TableField("sec_name")
    private String secName;
    @TableField("teacher_id")
    private String teacherId;
    private String semester;
    private BigDecimal year;
    @TableField("room_no")
    private String roomNo;
    private Integer number;

}
