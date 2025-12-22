package com.example.core.mapper;

import com.example.core.dto.ProductDetailsRequestDto;
import com.example.core.dto.ProductDetailsResponseDto;
import com.example.core.entity.Product;
import com.example.core.entity.ProductDetails;

public class ProductDetailsMapper {

    public static ProductDetailsResponseDto toDto(ProductDetails details) {
        ProductDetailsResponseDto dto = new ProductDetailsResponseDto();
        dto.setId(details.getId());
        dto.setDescription(details.getDescription());
        dto.setManufacturer(details.getManufacturer());
        dto.setWeight(details.getWeight());
        dto.setColor(details.getColor());
        dto.setProductId(details.getProduct().getId());
        return dto;
    }

    public static ProductDetails toEntity(ProductDetailsRequestDto dto) {
        ProductDetails details = new ProductDetails();
        details.setDescription(dto.getDescription());
        details.setManufacturer(dto.getManufacturer());
        details.setWeight(dto.getWeight());
        details.setColor(dto.getColor());

        // Assuming only the product ID is set in DTO. You need to set the Product reference here
        Product product = new Product();
        product.setId(dto.getProductId());
        details.setProduct(product);

        return details;
    }
}
