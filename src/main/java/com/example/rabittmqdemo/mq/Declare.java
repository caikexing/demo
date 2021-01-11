package com.example.rabittmqdemo.mq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ队列定义与绑定
 */
@Configuration
public class Declare {

    /**
     * 定义队列
     * @param userQueueName  队列名称
     * @return
     */
    @Bean
    public Queue userQueue(@Value("${app.rabbitmq.queue.user}") String userQueueName) {
        return QueueBuilder
                .durable(userQueueName)
                //.withArgument("x-max-length", 2)
                .build();
    }

    /**
     *
     * 定义交换机
     * @param userExchangeName  交换机名称
     * @return
     */

    @Bean
    public Exchange userExchange(@Value("${app.rabbitmq.exchange.user}") String userExchangeName) {
        return ExchangeBuilder
                .topicExchange(userExchangeName)
                .durable(true)
                .build();
    }

    /***
     *
     *
     *队列交换机绑定
     * @param userQueue
     * @param userExchange
     * @return
     */
    @Bean
    public Binding userBinding(Queue userQueue, Exchange userExchange) {
        return BindingBuilder
                .bind(userQueue)
                .to(userExchange)
                .with("user.*")
                .noargs();
    }
}
