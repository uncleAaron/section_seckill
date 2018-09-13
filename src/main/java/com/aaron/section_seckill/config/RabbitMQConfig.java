package com.aaron.section_seckill.config;

import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * for section_seckill
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/10
 */
@Configuration
public class RabbitMQConfig {
//
//    /**
//     * 交换配置
//     *
//     * @return
//     */
//    @Bean
//    public DirectExchange messageDirectExchange() {
//        return (DirectExchange) ExchangeBuilder.directExchange(QueueConstants.TAKES_EXCHANGE)
//                .durable(true)
//                .build();
//    }
//
//    /**
//     * 消息队列声明
//     *
//     * @return
//     */
//    @Bean
//    public Queue messageQueue() {
//        return QueueBuilder.durable(QueueConstants.TAKES_QUEUE)
//                .build();
//    }
//
//    /**
//     * 消息绑定
//     *
//     * @return
//     */
//    @Bean
//    public Binding messageBinding() {
//        return BindingBuilder.bind(messageQueue())
//                .to(messageDirectExchange())
//                .with(QueueConstants.TAKES_ROUTE_KEY);
//    }


//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//        return rabbitTemplate;
//    }
}
