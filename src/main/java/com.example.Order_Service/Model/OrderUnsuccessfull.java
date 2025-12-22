package com.example.Order_Service.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "order_unsuccessful")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUnsuccessfull {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            mappedBy = "orderUnsuccessfull",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<OrderItem> orderItems;

    private double totalPrice;
    private boolean isPaid;
    private String paymentStatus;
    private Long userId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime moveTime;
}
