package com.example.core.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product_details")
public class ProductDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String manufacturer;

    private Double weight;

    private String color;

    @OneToOne
    @JoinColumn(name = "product_id", unique = true, nullable = false)
    private Product product;

}
