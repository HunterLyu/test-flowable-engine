package com.hunt.example.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {

    public static final String TEST_FLOWABLE = "task1FinishTopic";

    // 创建一个Topic并设置分区数为2，分区副本数为2
    @Bean
    public NewTopic initialTopic() {
        return new NewTopic(TEST_FLOWABLE, 2, (short) 2);
    }
}