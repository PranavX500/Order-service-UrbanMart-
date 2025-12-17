package com.example.Order_Service.Repositery;

import com.example.Order_Service.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepositery extends JpaRepository<Order,Long> {
    List<Order> findByIdIn(List<Long> ids);
    @Query("SELECT o FROM Order o WHERE o.isPaid = false AND o.createdAt < :cutoffTime")
    List<Order> findUnpaidOrdersBefore(@Param("cutoffTime") LocalDateTime cutoffTime);
    @Query("SELECT  o.userId, i.pname, i.price, i.productId,o.isPaid " +
            "FROM Order o LEFT JOIN o.orderItems i " +
            "WHERE o.userId = :userId AND o.isPaid=true")
    List<Object[]> getOrderDetailsByUserId(@Param("userId") Long userId);
}
