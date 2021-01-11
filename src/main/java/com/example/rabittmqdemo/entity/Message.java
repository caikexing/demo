package com.example.rabittmqdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "message")
public class Message implements Serializable {
    @Id
    @Column(name = "id")
    private String id;
    /**
     * 消息承载的业务数据
     */
    @Column(name = "msg_data")
    private String msgData;


    /**
     * 交换机名称
     */
    @Column(name = "exchange_name")
    private String exchangeName;

    /**
     * 路由键
     */
    @Column(name = "routing_key")
    private String routingKey;

    /**
     * 消息状态
     *
     * @see com.futao.springboot.learn.rabbitmq.doc.reliabledelivery.model.enums.MessageStatusEnum
     */
    @Column(name = "status")
    private int status;

    /**
     * 重试次数
     */
    @Column(name = "retry_times")
    private int retryTimes;

    /**
     * 下一次重试时间
     */
    @Column(name = "next_retry_date_time")
    private LocalDateTime nextRetryDateTime;

    public Message(String id, String msgData, String exchangeName, String routingKey, int status, int retryTimes, LocalDateTime nextRetryDateTime) {
        this.id = id;
        this.msgData = msgData;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.status = status;
        this.retryTimes = retryTimes;
        this.nextRetryDateTime = nextRetryDateTime;
    }

    //添加默认构造器
    public Message() {

    }
}
