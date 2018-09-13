package com.aaron.section_seckill.mapper;

import com.aaron.section_seckill.entity.Score;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author aaron123
 * @since 2018-09-07
 */
public interface ScoreMapper extends BaseMapper<Score> {

    /**
     * 支持主键回填的score插入，详情在mapper看
     * @param score
     * @return
     */
    int insertEmptyScore(Score score);

}
