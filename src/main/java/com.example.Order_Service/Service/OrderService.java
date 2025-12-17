package com.example.Order_Service.Service;

import com.example.Order_Service.DTO.ItemDTO;
import com.example.Order_Service.Model.Order;
import com.example.Order_Service.Repositery.OrderRepositery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class OrderService {
    @Autowired
    OrderRepositery orderRepositery;


    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void RemovePendingOrders(){
        LocalDateTime cuttoff= LocalDateTime.now().minusMinutes(10);
        List<Order> pendingOrders=orderRepositery.findUnpaidOrdersBefore(cuttoff);
        for (Order order :  pendingOrders) {
            orderRepositery.delete(order);
            System.out.println("Deleted unpaid order: " + order.getId());
        }
    }

    public List<ItemDTO> getOrderBYuserid(Long userId){
       List<Object[]> order=orderRepositery.getOrderDetailsByUserId(userId);
        List<ItemDTO>results=new ArrayList<>();
       for( Object[] row:order) {
           ItemDTO dto=new ItemDTO();

           dto.setUserId((Long) row[0]);
           dto.setPname((String) row[1]);
           dto.setPrice((Double) row[2]);
           dto.setProductId((Long) row[3]);

           results.add(dto);
       }

        return results;


    }



}
