package com.example.Order_Service.Service;

import com.example.Order_Service.DTO.PaymentSuccessEvent;
import com.example.Order_Service.Model.Order;
import com.example.Order_Service.Repositery.OrderRepositery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {
    @Autowired
private OrderRepositery orderRepository;

    @KafkaListener(topics = "payment-success-topic", groupId = "order-group")
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        System.out.println("Received payment event for order: " + event.getOrderId());

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setPaid(true);
        order.setPaymentStatus("Payed");
        orderRepository.save(order);

        System.out.println("Order marked as COMPLETED for orderId: " + event.getOrderId());
    }

}
