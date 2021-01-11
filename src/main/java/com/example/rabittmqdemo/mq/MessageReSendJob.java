package com.example.rabittmqdemo.mq;

import com.alibaba.fastjson.JSON;
import com.example.rabittmqdemo.entity.Message;
import com.example.rabittmqdemo.entity.enums.MessageStatusEnum;
import com.example.rabittmqdemo.mapper.MessageMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.*;
import com.xxl.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 扫描数据库中需要重新投递的消息并重新投递
 *
 */

@Component
public class MessageReSendJob extends IJobHandler {
    private static  final Logger logger = LoggerFactory.getLogger(MessageReSendJob.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    private MessageMapper messageMapper;

    @Autowired
    private MessageReSendJob messageReSendJob;

    /**
     * 最大重试次数
     */
    @Value("${app.rabbitmq.retry.max-retry-times}")
    private int retryTimes;

    /**
     * 重试时间间隔
     */
    @Value("${app.rabbitmq.retry.retry-interval}")
    private int retryInterval;

    /**
     * 批量从数据库中读取的消息
     */
    private static final int PAGE_SIZE = 100;


    @XxlJob(value = "MessageReSendJob", init = "jobHandlerInit", destroy = "jobHandlerDestroy")
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        long startTime = System.currentTimeMillis();
        logger.info("开始扫描需要进行重试投递的消息");
        XxlJobLogger.log("开始扫描需要进行重试投递的消息");
        service(1);
        logger.info("扫描需要进行重试投递的消息任务结束，耗时[{}]ms", System.currentTimeMillis() - startTime);
        XxlJobLogger.log("扫描需要进行重试投递的消息任务结束，耗时[{}]ms", System.currentTimeMillis() - startTime);
        return ReturnT.SUCCESS;
    }

    public void service(int pageNum) {
        PageHelper.startPage(pageNum,PAGE_SIZE);
        List<Message> mess = messageMapper.selectByStatus(MessageStatusEnum.SENDING.getStatus(), new Date());
        PageInfo<Message> messageIPage = new PageInfo<>(mess);
        /*IPage<Message> messageIPage = messageMapper.selectPage(new Page<>(pageNum, PAGE_SIZE),
                Wrappers.<Message>lambdaQuery()
                        //发送中的消息
                        .eq(Message::getStatus, MessageStatusEnum.SENDING.getStatus())
                        //已到达下次发送时间
                        .le(Message::getNextRetryDateTime, LocalDateTime.now(ZoneOffset.ofHours(8)))
        );*/
        List<Message> messages = messageIPage.getList();
        int a = retryTimes;
        messages.forEach(message -> {
            if (retryTimes <= message.getRetryTimes()) {
                //已达到最大投递次数，将消息设置为投递失败
                messageMapper.updateById(MessageStatusEnum.FAIL.getStatus(),message.getId());
                // messageMapper.update(null, Wrappers.<Message>lambdaUpdate().set(Message::getStatus, MessageStatusEnum.FAIL.getStatus()).set(Message::getUpdateDateTime, LocalDateTime.now(ZoneOffset.ofHours(8))).eq(Message::getId, message.getId()));
            } else {
                messageReSendJob.reSend(message);
            }
        });
        if (PAGE_SIZE == messages.size()) {
            service(++pageNum);
        }
    }

    /**
     * 重新投递消息
     *
     * @param message
     */
    public void reSend(Message message) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND,retryInterval);
        date = cal.getTime();
        logger.info("到这里了吗？？？？？");
        int retry = message.getRetryTimes()+1;
        messageMapper.update(MessageStatusEnum.SENDING.getStatus(),retry,message.getId());
       // messageMapper.deleteById(message.getId());
        /*messageMapper.update(null,
                Wrappers.<Message>lambdaUpdate()
                        .set(Message::getRetryTimes, message.getRetryTimes() + 1)
                        .set(Message::getNextRetryDateTime, LocalDateTime.now(ZoneOffset.ofHours(8)).plus(retryInterval))
                        .set(Message::getUpdateDateTime, LocalDateTime.now(ZoneOffset.ofHours(8)))
                        .eq(Message::getId, message.getId())
        );*/
        try {
            //再次投递
            rabbitTemplate.convertAndSend(message.getExchangeName(), message.getRoutingKey(), message.getMsgData(), new CorrelationData(message.getId()));
        } catch (Exception e) {
            logger.error("消息[{}]投递失败", JSON.toJSONString(message));
        }
    }

    public void jobHandlerInit() {
        logger.info("before job execute...");
        XxlJobLogger.log("before job handler init...");
    }

    public void jobHandlerDestroy() {
        logger.info("after job execute...");
        XxlJobLogger.log("after job execute...");
    }
}
