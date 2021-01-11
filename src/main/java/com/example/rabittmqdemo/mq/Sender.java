package com.example.rabittmqdemo.mq;

import com.alibaba.fastjson.JSON;
import com.example.rabittmqdemo.entity.Message;
import com.example.rabittmqdemo.entity.User;
import com.example.rabittmqdemo.entity.enums.MessageStatusEnum;
import com.example.rabittmqdemo.mapper.MessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
public class Sender {
    private static  final Logger logger = LoggerFactory.getLogger(Sender.class);
    // 每次重试时间间隔
    @Value("${app.rabbitmq.retry.retry-interval}")
    private Duration retryInterval;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    private MessageMapper messageMapper;

    //  交换机定义
    @Value("${app.rabbitmq.exchange.user}")
    private String userExchangeName;

        public void send(User user) {
        Message message = Message.builder()
                .msgData(JSON.toJSONString(user))
                .exchangeName(userExchangeName)
                .routingKey("user.abc")
                .status(MessageStatusEnum.SENDING.getStatus())
                //下次重试时间
                .nextRetryDateTime(LocalDateTime.now(ZoneOffset.ofHours(8)).plus(retryInterval))
                .retryTimes(0)
                .id(UUID.randomUUID().toString())
                .build();

        //消息落库
        messageMapper.save(message);
        logger.info(">>>>>>>>>>> 消息开始投递.");
        CorrelationData correlationData = new CorrelationData(message.getId());
        //消息投递到MQ
        rabbitTemplate.convertAndSend(userExchangeName, "user.abc", JSON.toJSONString(user), correlationData);
    }
}
