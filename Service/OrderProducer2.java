package com.example.Order_Service.Service;


import com.example.Order_Service.DTO.RequestToPayment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer2 {
    private final KafkaTemplate<String ,RequestToPayment> kafkaTemplate;


    public OrderProducer2(KafkaTemplate<String, RequestToPayment> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }
    public void orderIds(RequestToPayment requestToPayment){
        kafkaTemplate.send("Orders",requestToPayment);
        System.out.println(requestToPayment);
        System.out.println(" Sent event to Kafka: " + requestToPayment);
    }




}
