package com.example.core.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductDetailsRequestDto {

    @NotBlank(message = "Description must not be blank")
    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @NotBlank(message = "Manufacturer must not be blank")
    @Size(max = 100, message = "Manufacturer must be at most 100 characters")
    private String manufacturer;

    @NotNull(message = "Weight must not be null")
    @Positive(message = "Weight must be greater than zero")
    private Double weight;

    @NotBlank(message = "Color must not be blank")
    @Size(max = 50, message = "Color must be at most 50 characters")
    private String color;

    @NotNull(message = "Product ID must not be null")
    @Positive(message = "Product ID must be a positive number")
    private Long productId;
}
