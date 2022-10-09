package com.lms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.lms.Models.EmailDetails;
import com.lms.email.EmailService;

@Service
public class KafkaConsumerListener {

    private static final String TOPIC = "Kafka-Email-topic";
    
    @Autowired
    EmailService emailService;

    @KafkaListener(topics = TOPIC, groupId="group_id", containerFactory = "userKafkaListenerFactory")
    public void consumeJson(String emailDetails) 
    {
        String[] data = emailDetails.split("\\|");
        EmailDetails ed=  new EmailDetails();
        ed.setSubject("Course Avaliability Status Update");
        ed.setRecipient(data[1]);
        ed.setMsgBody(data[0]);
        System.out.println("Email Status :"+ emailService.sendSimpleMail(ed));

    }
    
}