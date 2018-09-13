package com.aaron.section_seckill.service.impl;

import com.aaron.section_seckill.constant.RedisConstants;
import com.aaron.section_seckill.constant.enums.StatusEnum;
import com.aaron.section_seckill.entity.Score;
import com.aaron.section_seckill.entity.Takes;
import com.aaron.section_seckill.exception.TakeException;
import com.aaron.section_seckill.mapper.ScoreMapper;
import com.aaron.section_seckill.mapper.SectionMapper;
import com.aaron.section_seckill.mapper.TakesMapper;
import com.aaron.section_seckill.utils.RedisDao;
import com.aaron.section_seckill.service.TakeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * <p>
 * for section_manage
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/8
 */
@Service
@Slf4j
public class TakeServiceImpl implements TakeService {

    @Autowired
    TakesMapper takesMapper;
    @Autowired
    SectionMapper sectionMapper;
    @Autowired
    ScoreMapper scoreMapper;
    @Autowired
    RedisDao redisDao;

    private static final String TAKE_STATUS = "'takesStatus'";

    /**
     * 选课结束状态，从mysql获取并使用redis暂存选课状态信息，key为takesStatus，value为true false
     */
    @Cacheable(cacheNames = {"takes"}, key = TAKE_STATUS)
    public boolean getTakeStatus() {
        return takesMapper.getTakeStatus();
    }

    @Override
    @Transactional(rollbackFor = TakeException.class)
    public boolean takeSection(Takes takes) throws TakeException {
        try {
            // 先记录了成功选课行为，后续验证是否选课成功，选课不成功则回滚
            int insertCount = takesMapper.tryInsert(takes.getSecId(), takes.getStudentId());
            if (insertCount <= 0) {
                // 重复选课
                throw new TakeException(StatusEnum.TAKE_FAILED, takes.getStudentId(), takes.getSecId());
            } else {
                // 减余量
                int reduceCount = sectionMapper.reduceNumber(takes.getSecId());
                if (reduceCount <= 0) {
                    throw new TakeException(StatusEnum.TAKE_FAILED, takes.getStudentId(), takes.getSecId());
                } else {
                    // 选课成功之后，设置成绩单
                    Score score = new Score();
                    scoreMapper.insertEmptyScore(score);
                    takesMapper.updateScoreId(takes.getSecId(), takes.getStudentId(), score.getScoreId());
                }
            }
            log.info("TakeService :: takes Success : " + takes);
        } catch (TakeException ex) {
            // 事务异常，回滚
            log.warn("TakeService :: takes Failed : " + takes);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            // 失败的抢课记录到redis的failed_list中
            redisDao.ZADD(RedisConstants.FAILED_ZSET, takes.toRecord(), 1L);
            return false;
        }
        // 成功的抢课记录到redis的success_list中
        redisDao.ZADD(RedisConstants.SUCCESS_ZSET, takes.toRecord(), 1L);
        return true;
    }

    @CachePut(cacheNames = {"takes"}, key = TAKE_STATUS)
    public boolean saveOrSetTakeStatus(boolean status) {
        int insertCount = takesMapper.trySaveTakeStatus(status);
        if (insertCount <= 0) {
            // 没有插入数据，则执行更新
            takesMapper.setTakeStatus(status);
        }
        return status;
    }

    @Override
    public int checkSuccess(String stuid, String secid) {
        Takes takes = new Takes();
        takes.setSecId(secid).setStudentId(stuid);
        boolean inSuccessList = redisDao.ZREM(RedisConstants.SUCCESS_ZSET, takes.toRecord());
        if (inSuccessList) {
            return 1;   // 处理完毕，结果是成功的
        }
        boolean inFailedList = redisDao.ZREM(RedisConstants.FAILED_ZSET, takes.toRecord());
        if (inFailedList) {
            return -1;  // 处理完毕，结果是失败的
        }
        return 0;   // 还没处理完，没放进队列
    }

}
