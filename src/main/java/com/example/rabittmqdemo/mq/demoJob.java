package com.example.rabittmqdemo.mq;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Component
public class demoJob extends IJobHandler  {
    private static  final Logger logger = LoggerFactory.getLogger(demoJob.class);

    @XxlJob("demoJobHandler")
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("XXL-JOB, Hello World.");

        logger.info("开始执行定时demo-------------------");
        for (int i = 0; i < 5; i++) {
            XxlJobLogger.log("beat at:" + i);
            TimeUnit.SECONDS.sleep(2);

        }

        logger.info("结束执行定时demo-------------------");
        // default success
        return ReturnT.SUCCESS;
    }
}
