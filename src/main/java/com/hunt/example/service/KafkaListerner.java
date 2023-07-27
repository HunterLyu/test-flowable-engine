package com.hunt.example.service;

import com.hunt.example.config.MessageConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class KafkaListerner {
    protected Logger log = LoggerFactory.getLogger(getClass());


    @KafkaListener(topics = {"task1FinishTopic", "task1SendTopic", "task2FinishTopic", "task2SendTopic",
            "task3SendTopic", "task3FinishTopic"})
    public void onMessage1(ConsumerRecord<?, ?> record) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("简单消费：" + record.topic() + "-" + record.partition() + "-" + record.value());

    }
}
