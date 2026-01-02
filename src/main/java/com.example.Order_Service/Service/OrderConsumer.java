package com.example.Order_Service.Service;

import com.example.Order_Service.DTO.*;
import com.example.Order_Service.Model.Order;
import com.example.Order_Service.Model.OrderItem;
import com.example.Order_Service.Repositery.OrderRepositery;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderConsumer {
    @Autowired
    private OrderRepositery orderRepositery;
    @Autowired
    OrderProducer2 producer2;


    @KafkaListener(topics = "product-success-topic", groupId = "order-group", containerFactory = "kafkaListenerContainerFa")

    public void handleProductList(ProductResponseEvent event) {
        System.out.println(event);
        try {
            List<ProductDto> products = event.getProducts();


            if (products == null || products.isEmpty()) {
                throw new IllegalArgumentException("Received empty product list!");
            }

            double totalPrice = 0.0;
            List<OrderItem> orderItems = new ArrayList<>();

            for (ProductDto product : products) {
                if (product.getPrice() < 0) {
                    throw new RuntimeException("Invalid product price: " + product.getPrice());
                }

                OrderItem item = new OrderItem();
                item.setProductId(product.getProductId());
                item.setPname(product.getProductName());
                item.setPrice(product.getPrice());
                item.setQuantity(product.getQuantity());
                orderItems.add(item);
                totalPrice += product.getPrice()*product.getQuantity();
            }

            Order order = new Order();


            order.setOrderItems(orderItems);
            for (OrderItem oi : orderItems) oi.setOrder(order);

            order.setUserId(event.getUserId());
            order.setTotalPrice(totalPrice);
            order.setPaid(false);
            order.setPaymentStatus("Pending");

            orderRepositery.save(order);

            RequestToPayment r1=new RequestToPayment();
            r1.setOrderId(order.getId());
            r1.setTotalPrice(order.getTotalPrice());
            r1.setRequestId(event.getRequestId());
            r1.setEmailId(event.getEmailId());
            r1.setUserId(event.getUserId());


            producer2.orderIds(r1);


            System.out.println(r1);
        }
        catch (Exception e) {
            System.err.println("error processing order: " + e.getMessage());
         
        }
    }

    public Order getOrderById(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("Invalid order ID");
        }

        return orderRepositery.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found for ID: " + orderId));
    }
}

