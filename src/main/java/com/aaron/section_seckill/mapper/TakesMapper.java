package com.aaron.section_seckill.mapper;

import com.aaron.section_seckill.entity.Takes;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author aaron123
 * @since 2018-09-07
 */
public interface TakesMapper extends BaseMapper<Takes> {

    /**
     * 主键冲突时不报错，返回0
     */
    int tryInsert(@Param("secId") String secId, @Param("studentId") String studentId);

    int countTakeBySection(String secId);

    Takes selectById(String secId, String studentId);

    int updateScoreId(@Param("secId") String secId, @Param("studentId") String studentId, @Param("scoreId") int scoreId);

    int trySaveTakeStatus(boolean status);

    boolean getTakeStatus();

    boolean setTakeStatus(boolean status);
}
