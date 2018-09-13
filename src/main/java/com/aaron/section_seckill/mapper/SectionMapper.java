package com.aaron.section_seckill.mapper;

import com.aaron.section_seckill.entity.Section;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author aaron123
 * @since 2018-09-07
 */
public interface SectionMapper extends BaseMapper<Section> {


    int reduceNumber(String secId);
}
