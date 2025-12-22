package com.example.core.dto;

import lombok.Data;

@Data
public class ProductDetailsResponseDto {
    private Long id;
    private String description;
    private String manufacturer;
    private Double weight;
    private String color;
    private Long productId;
}
