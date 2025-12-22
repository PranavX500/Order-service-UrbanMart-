package com.example.Order_Service.Controller;

import com.example.Order_Service.DTO.PollingData;
import com.example.Order_Service.DTO.PoolingDTO;
import com.example.Order_Service.Model.Pooling;
import com.example.Order_Service.Repositery.PoolingRepositery;
import com.example.Order_Service.Service.OrderService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin")
public class PollingController {


    @Autowired
    PoolingRepositery poolingRepositery;
    @Autowired
    OrderService orderService;


 @PostMapping("/polling")
 public ResponseEntity<?>updatePolling(@RequestBody PoolingDTO poolingDTO) {


     Pooling p1 = poolingRepositery.findPoolingById(Long.valueOf(1));
     LocalDateTime l1 = LocalDateTime.now();
     if (p1 == null) {
         Pooling p2=new Pooling();
         p2.setId(1L);
         p2.setPoolingEnable(poolingDTO.getPoolingEnable());
         p2.setPollingDate(l1);
         poolingRepositery.save(p2);

     } else {

         p1.setPoolingEnable(poolingDTO.getPoolingEnable());
         p1.setPollingDate(l1);
         poolingRepositery.save(p1);
         System.out.println(p1);


     }
     return ResponseEntity.ok("Pooling started");

 }

 @GetMapping("/GetPolling")
  public ResponseEntity<PollingData>getPollingData(){

      return ResponseEntity.ok(orderService.poolingDTO());
 }
}
