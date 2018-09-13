package com.aaron.section_seckill.utils;

import com.aaron.section_seckill.constant.QueueConstants;
import com.aaron.section_seckill.entity.Takes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * <p>
 * for section_seckill
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/10
 */
@Slf4j
@Service
public class MQProvider {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendTakeMessage(String stuId, String secId) {
        Takes takes = new Takes();
        takes.setStudentId(stuId)
                .setSecId(secId);
        log.info("RabbitMQ :: send message: " + takes);
        rabbitTemplate.convertAndSend(QueueConstants.TAKES_EXCHANGE, QueueConstants.TAKES_ROUTE_KEY, takes);
    }
}
