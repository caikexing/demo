package com.example.rabittmqdemo.mq;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XXL-JOB配置
 *
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobConfig {
    private static  final Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

   /* @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.appname}")
    private String appname;

//    @Value("${xxl.job.executor.address}")
//    private String address;

    *//*@Value("${xxl.job.executor.ip}")
    private String ip;*//*

    @Value("${xxl.job.executor.port}")
    private int port;

    @Value("${xxl.job.executor.logpath}")
    private String logPath;

    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;


    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        logger.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppName(appname);
       // xxlJobSpringExecutor.setAddress(address);
       // xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);

        return xxlJobSpringExecutor;
    }*/
    private final Admin admin = new Admin();
    private final Executor executor = new Executor();
    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobConfig xxlJobConfig) {
        logger.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobConfig.getAdmin().getAddresses());
        xxlJobSpringExecutor.setAppName(xxlJobConfig.getExecutor().getAppName());
        xxlJobSpringExecutor.setIp(xxlJobConfig.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(xxlJobConfig.getExecutor().getPort());
        xxlJobSpringExecutor.setLogPath(xxlJobConfig.getExecutor().getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobConfig.getExecutor().getLogRetentionDays());
        return xxlJobSpringExecutor;
    }

    @Getter
    @Setter
    public static class Admin {
        private String addresses;
    }

    @Getter
    @Setter
    public static class Executor {
        private String appName;
        private String ip;
        private int port;
        private String logPath;
        private int logRetentionDays;
    }
}
