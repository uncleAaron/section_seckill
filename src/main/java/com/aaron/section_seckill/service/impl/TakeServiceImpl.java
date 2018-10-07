package com.aaron.section_seckill.service.impl;

import com.aaron.section_seckill.constant.RedisConstants;
import com.aaron.section_seckill.constant.enums.StatusEnum;
import com.aaron.section_seckill.entity.Score;
import com.aaron.section_seckill.entity.Section;
import com.aaron.section_seckill.entity.Takes;
import com.aaron.section_seckill.exception.TakeException;
import com.aaron.section_seckill.mapper.ScoreMapper;
import com.aaron.section_seckill.mapper.SectionMapper;
import com.aaron.section_seckill.mapper.TakesMapper;
import com.aaron.section_seckill.service.TakeService;
import com.aaron.section_seckill.utils.MQProvider;
import com.aaron.section_seckill.utils.RedisDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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
    @Autowired
    MQProvider mqProvider;

    private ReentrantLock lock = new ReentrantLock();

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
    public boolean takeSectionToDB(Takes takes) throws TakeException {
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

    /**
     * 1. 检查redis上是否已存在课程，若不存在，则从数据库读取一条section记录放到redis上(key为课程ID，value为余量，-1处理使用redis的原子-1操作)
     * 2. 检查该用户是否重复秒杀
     * 3. 若余量>0，则redis记录 -1 处理，同时将修改记录发送到 MQ 上
     * 4. 否则即余量<=0，直接拒绝
     * 超卖解决办法：1. 使用乐观锁，重试到成功为止 2. 使用队列，先往名为课程id的队列push所有的数量的库存，然后依次pop作为减库存，这样解决了原子操作的问题，但是会不会很臃肿？
     */
//    ReentrantLock lock = new ReentrantLock();

    @Override
    public boolean tryTake(String sectionId, String username) throws TakeException {
//        lock.lock();
        try {
            // 获取余量
            Integer lastNumber = (Integer) redisDao.GET(RedisConstants.SECTIONS_LIST, sectionId);
            if (lastNumber == null) {
                Section section = sectionMapper.selectById(sectionId);
                if (section == null) {
                    return false;   // 不存在该课程，返回拒绝
                } else {
                    redisDao.SET(RedisConstants.SECTIONS_LIST, sectionId, section.getNumber(), 10L, TimeUnit.MINUTES);  // 把课程更新上去
                }
            }
            // 检查用户是否已经秒杀过了
            long a = redisDao.ZRANK(RedisConstants.SECTIONS_LIST + ":" + sectionId + ":zset", username);
            if (a >= 0) {
                return false;
            }
            // 减余量，必须要用户还没秒杀且大于0才能-1，这就涉及并发，>0检查和-1结合后必须设计成原子操作，在此使用分布式锁
            boolean ok = seckillOnRedis(sectionId, username);
            return ok;
        } catch (TakeException ex) {
            return false;
        } finally {
//            lock.unlock();
        }
    }

    private boolean seckillOnRedis(String sectionId, String username) throws TakeException {
        boolean dercOK = false;
        long time = System.currentTimeMillis() + 1000 * 10;  //超时时间：10秒，最好设为常量
        try {
            // 加锁
            lock.lock();
//            boolean isLock = redisDao.lock(sectionId, String.valueOf(time));
//            while (!isLock) {
//                throw new TakeException(StatusEnum.FULL_FAILED);
//            }
            // 检查用户是否已经秒杀过了
            long a = redisDao.ZRANK(RedisConstants.SECTIONS_LIST + ":" + sectionId + ":zset", username);
            if (a >= 0) {
                dercOK = false;
            } else {
                // 查库存
                Integer redisNumber = (Integer) redisDao.GET(RedisConstants.SECTIONS_LIST, sectionId);
                if (redisNumber == null || redisNumber <= 0) {  // 没库存
                    dercOK = false;
                } else {
                    redisDao.DECR(RedisConstants.SECTIONS_LIST, sectionId);   // -1
                    dercOK = true;
                }
            }
            if (!dercOK) {  // 失败
                return false;
            } else {
                redisDao.ZADD(RedisConstants.SECTIONS_LIST + ":" + sectionId + ":zset", username, 1L);  // 记录重复选课数据
                mqProvider.sendTakeMessage(username, sectionId);    // 发送选课消息到mq
                return true;
            }
        } finally {
            //解锁
//            redisDao.unlock(sectionId, String.valueOf(time));
            lock.unlock();
        }
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
