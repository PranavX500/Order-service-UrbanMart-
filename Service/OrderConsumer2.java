package com.example.Order_Service.Service;

import com.example.Order_Service.DTO.RazorpayOrderResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderConsumer2 {

    private final Map<String, RazorpayOrderResponse> cache = new ConcurrentHashMap<>();

    public Map<String, RazorpayOrderResponse> getCache() {
        return cache;
    }

    @KafkaListener(
            topics = "Payment-Response",
            groupId = "order-group-response",
            containerFactory = "razorpayResponseListenerFactory"
    )
    public void handlePaymentResponse(RazorpayOrderResponse response) {
        System.out.println(response);

        cache.put(response.getRequestId(), response);

        System.out.println("🔥 Received Razorpay Order for requestId: "
                + response.getRequestId());
    }
}
