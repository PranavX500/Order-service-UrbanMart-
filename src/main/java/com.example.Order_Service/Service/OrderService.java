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
    @Transactional
    public void ShiftPendingOrders() {
        LocalDateTime cuttoff1 = LocalDateTime.now().minusMinutes(10);

        Pooling p1=poolingRepositery.findPoolingById(Long.valueOf(1));


        System.out.println("On");
        if (p1 == null || !"Y".equals(p1.getPoolingEnable())) {
            System.out.print("off");
            return;
        }
        if (p1.getPoolingEnable().equals("Y")) {
            OrderUnsuccessfull o1 = new OrderUnsuccessfull();

            LocalDateTime cuttoff = LocalDateTime.now().minusMinutes(1);
            LocalDateTime l1 = LocalDateTime.now();
            List<Order> pendingOrders = orderRepositery.findUnpaidOrdersBefore(cuttoff);
            for (Order order : pendingOrders) {



                try {
                    o1.setUserId(order.getUserId());

                    o1.setPaid(order.isPaid());

                    o1.setPaymentStatus(order.getPaymentStatus());

                    o1.setCreatedAt(order.getCreatedAt());

                    o1.setMoveTime(l1);
                    orderUnsuccessfullRepositery.save(o1);






                } catch (RuntimeException e) {
                    throw new RuntimeException("Error in moving order in archiver", e);
                }


            }
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
      public PollingData poolingDTO(){
        Pooling p1=poolingRepositery.findById(1L).orElseThrow(()->new RuntimeException("Id not found"));
        System.out.println(p1);
        PollingData p2=new PollingData();
        p2.setId(p1.getId());
        p2.setPoolingEnable(p1.getPoolingEnable());
        p2.setPollingDate(p1.getPollingDate());
        return  p2;


    }




}
