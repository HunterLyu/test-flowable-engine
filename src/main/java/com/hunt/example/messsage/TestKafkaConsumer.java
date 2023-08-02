package com.hunt.example.messsage;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TestKafkaConsumer {
    protected Logger log = LoggerFactory.getLogger(getClass());


    @KafkaListener(topics = {"task1FinishTopic", "task1SendTopic", "task2FinishTopic", "task2SendTopic",
            "task3SendTopic", "task3FinishTopic"})
    public void onMessage1(ConsumerRecord<?, ?> record) {
        log.info("consume message of:：" + record.topic() + "-" + record.partition() + "-" + record.value());
    }
}
