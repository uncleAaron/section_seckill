package com.aaron.section_seckill.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class Takes implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("sec_id")
    private String secId;
    @TableField("student_id")
    private String studentId;
    @TableField("score_id")
    private Integer scoreId;

    public String toRecord() {
        return secId+"_"+studentId;
    }

}
