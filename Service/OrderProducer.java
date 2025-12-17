package com.example.Order_Service.Service;

import com.example.Order_Service.DTO.ProductRequestEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
    private final KafkaTemplate<String , ProductRequestEvent>kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, ProductRequestEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendIds(ProductRequestEvent event){

        kafkaTemplate.send("Productids",event);
        System.out.println(" Sent event to Kafka: " + event);
    }
}
