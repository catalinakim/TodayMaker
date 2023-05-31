package com.todaymaker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ThreadPoolSizeChecker implements CommandLineRunner {

    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    public ThreadPoolSizeChecker(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void run(String... args) throws Exception {
        if (taskExecutor.getThreadPoolExecutor() != null) {
            int threadPoolSize = taskExecutor.getThreadPoolExecutor().getMaximumPoolSize();
            System.out.println("Thread pool size: " + threadPoolSize);
        }
    }
}