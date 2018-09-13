package com.aaron.section_seckill.entity;

import com.baomidou.mybatisplus.annotations.TableId;
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
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    private String name;
    private String password;

}
