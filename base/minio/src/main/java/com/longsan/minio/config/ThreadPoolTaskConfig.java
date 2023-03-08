package com.longsan.minio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ThreadPoolTaskConfig {

    @Bean("threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 基础线程数
        executor.setCorePoolSize(16);
        // 阻塞队列数
        executor.setQueueCapacity(124);
        // 最大线程数量 队列已满并且线程达到这个数，就会抛出异常
        executor.setMaxPoolSize(64);
        // 线程空闲存活时间
        executor.setKeepAliveSeconds(60);
        // 线程池名称
        executor.setThreadGroupName("lhTask-");
        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}
