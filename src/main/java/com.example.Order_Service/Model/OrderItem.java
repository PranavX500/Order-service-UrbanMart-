package com.example.Order_Service.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="OrderItem")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;
    @ManyToOne
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude// The foreign key in OrderItem table
    private Order order;
    @Column(nullable = false)
    private Long   productId;
    @Column(nullable = false)
    private String pname;
    @Column(nullable = false)
    private Double price;


}
