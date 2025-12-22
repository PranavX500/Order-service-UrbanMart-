package com.example.Order_Service.Repositery;

import com.example.Order_Service.Model.OrderUnsuccessfull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderUnsuccessfullRepositery extends JpaRepository<OrderUnsuccessfull,Long> {
}
