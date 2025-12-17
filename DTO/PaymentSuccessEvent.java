package com.example.Order_Service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessEvent {
    private Long orderId;
    private double totalprice;
    private String status;
    private LocalDateTime paymentTime;
}
