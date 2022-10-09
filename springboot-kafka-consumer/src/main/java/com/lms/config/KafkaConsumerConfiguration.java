package com.lms.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.lms.Models.EmailDetails;

@EnableKafka
@Configuration
public class KafkaConsumerConfiguration {

    @Bean
    public ConsumerFactory<String, String> userConsumerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        config.put("key.deserializer", 
                "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", 
                "org.apache.kafka.common.serialization.StringDeserializer");
        return new DefaultKafkaConsumerFactory<String, String>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> userKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userConsumerFactory());
        return factory;
    }

}
