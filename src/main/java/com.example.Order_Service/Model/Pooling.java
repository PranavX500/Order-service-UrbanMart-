package com.example.Order_Service.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Pooling_Config")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Pooling {
    @Id

    private   Long id;
     private   String PoolingEnable;
     @CreationTimestamp
     private LocalDateTime PollingDate;


}
