package com.hunt.example.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {


//    @Value("${spring.kafka.producer.transaction-id-prefix}")
//    private String transactionPrefix;

    // 创建一个Topic并设置分区数为2，分区副本数为2
//    @Bean
//    public NewTopic initialTopic() {
//        return new NewTopic(TEST_FLOWABLE, 2, (short) 2);
//    }


//    @Bean("specialKafkaTransactionManager")
//    @Primary
//    public KafkaTransactionManager<?, ?> kafkaTransactionManager(DefaultKafkaProducerFactory<?, ?> producerFactory) {
//        producerFactory.setTransactionIdPrefix("kafka-tx-");
//        return new KafkaTransactionManager(producerFactory);
//    }


}