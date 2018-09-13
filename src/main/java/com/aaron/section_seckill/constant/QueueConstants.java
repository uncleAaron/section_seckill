package com.aaron.section_seckill.constant;

/**
 * <p>
 * 之前的都是采用的Enum方式来配置队列相关的Exchange、Name、 RouteKey等相关的信息，使用枚举有个弊端，无法在注解内作为属性的值使用，
 * 所以之前的Consumer类配置监听的队列时都是字符串的形式，这样后期修改时还要修改多个地方（当然队列信息很少变动），
 * 现在使用Constants常量的形式进行配置
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/10
 */
public interface QueueConstants {
    String TAKES_EXCHANGE = "takes.fanout";
    String TAKES_QUEUE = "section.takes";
    String TAKES_ROUTE_KEY = "takes";
}
