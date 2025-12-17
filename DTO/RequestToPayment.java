package com.example.Order_Service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestToPayment {
    private Double totalPrice;

    private Long orderId;
    private String requestId;
    private String emailId;
    private Long UserId;



}
