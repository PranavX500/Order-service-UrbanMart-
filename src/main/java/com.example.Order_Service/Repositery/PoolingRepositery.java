package com.example.Order_Service.Repositery;

import com.example.Order_Service.Model.Pooling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoolingRepositery extends JpaRepository<Pooling,Long> {
    Pooling findPoolingById(Long id);
}
