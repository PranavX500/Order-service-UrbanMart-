package com.example.Order_Service.Controller;

import com.example.Order_Service.DTO.*;

import com.example.Order_Service.Model.Order;
import com.example.Order_Service.Service.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/Order")
public class OrderController {

    @Autowired
    OrderProducer orderProducer;

    @Autowired
    OrderConsumer orderConsumer;

    @Autowired
    OrderProducer2 orderProducer2;

    @Autowired
    OrderConsumer2 orderConsumer2;

    @Autowired
    OrderService orderService;



    @GetMapping("/pay-result/{requestId}")
    public RazorpayOrderResponse getPaymentResult(
            @PathVariable String requestId

            ) throws Exception {
        int retries = 20;

        while (retries-- > 0) {

            RazorpayOrderResponse response = orderConsumer2
                    .getCache()
                    .get(requestId);

            if (response != null) {
                orderConsumer2.getCache().remove(requestId);
                return response;
            }

            Thread.sleep(500);
        }


        throw new RuntimeException("Payment Service did not respond for requestId: " + requestId);

    }


    @PostMapping("/Order-Summary")
    public ResponseEntity<?> createOrder(
            @RequestBody ProductRequestEvent productIds,
            @RequestHeader("X-EMAIL") String email,
            @RequestHeader("X-UserId") Long userId

    ) {
        productIds.setEmailId(email);
        productIds.setUserId(userId);
        System.out.println(productIds);
        orderProducer.sendIds(productIds);

        return ResponseEntity.ok(
                Map.of("requestId", productIds.getRequestId())
        );
    }

  @GetMapping("/GetOrder")
   public ResponseEntity<Object>getorder( @RequestHeader("X-UserId") Long userId){
    return  ResponseEntity.ok(orderService.getOrderBYuserid(userId));

  }



}




