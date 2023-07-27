package com.hunt.example.messsage;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaListener {
    protected Logger log = LoggerFactory.getLogger(getClass());


    @org.springframework.kafka.annotation.KafkaListener(topics = {"task1FinishTopic", "task1SendTopic", "task2FinishTopic", "task2SendTopic",
            "task3SendTopic", "task3FinishTopic"})
    public void onMessage1(ConsumerRecord<?, ?> record) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("简单消费：" + record.topic() + "-" + record.partition() + "-" + record.value());

    }
}
