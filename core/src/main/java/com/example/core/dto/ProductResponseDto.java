package com.example.core.dto;

import lombok.Data;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private String sku;
    private Double price;
    private Integer quantity;
    private Long warehouseId;
}
