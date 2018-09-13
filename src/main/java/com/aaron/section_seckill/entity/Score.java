package com.aaron.section_seckill.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
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
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "score_id", type = IdType.AUTO)
    private Integer scoreId;
    @TableField("score_attend_1")
    private BigDecimal scoreAttend1;
    @TableField("score_attend_2")
    private BigDecimal scoreAttend2;
    @TableField("score_attend_3")
    private BigDecimal scoreAttend3;
    @TableField("score_quiz_1")
    private BigDecimal scoreQuiz1;
    @TableField("score_quiz_2")
    private BigDecimal scoreQuiz2;
    @TableField("score_quiz_3")
    private BigDecimal scoreQuiz3;
    @TableField("score_homework_1")
    private BigDecimal scoreHomework1;
    @TableField("score_homework_2")
    private BigDecimal scoreHomework2;
    @TableField("score_homework_3")
    private BigDecimal scoreHomework3;
    @TableField("score_finalexam")
    private BigDecimal scoreFinalexam;

}
