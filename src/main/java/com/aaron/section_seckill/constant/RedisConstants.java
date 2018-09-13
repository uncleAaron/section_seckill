package com.aaron.section_seckill.constant;

/**
 * <p>
 * for section_seckill
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/10
 */
public interface RedisConstants {
    String LOCKED_PREFIX = "locks";
    String FAILED_ZSET = "failed_list";
    String SUCCESS_ZSET = "success_list";
    String RATE_LIMIT = "rate_limit";
    String SECTIONS_LIST = "sectionlist";
}
