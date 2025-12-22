package com.example.core.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductRequestDto {

    @NotBlank(message = "Product name must not be blank")
    @Size(max = 150, message = "Product name must be at most 150 characters")
    private String name;

    @NotBlank(message = "SKU must not be blank")
    @Size(max = 50, message = "SKU must be at most 50 characters")
    private String sku;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be zero or greater")
    private Integer quantity;

    @NotNull(message = "Warehouse ID is required")
    @Positive(message = "Warehouse ID must be a positive number")
    private Long warehouseId;
}
