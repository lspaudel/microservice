package com.example.core.mapper;

import com.example.core.dto.ProductRequestDto;
import com.example.core.dto.ProductResponseDto;
import com.example.core.entity.Product;
import com.example.core.entity.Warehouse;

public class ProductMapper {

    public static ProductResponseDto toDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSku(product.getSku());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setWarehouseId(product.getWarehouse().getId());
        return dto;
    }

    public static Product toEntity(ProductRequestDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setSku(dto.getSku());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        // Set Warehouse reference by ID only (assuming only ID is passed)
        Warehouse warehouse = new Warehouse();
        warehouse.setId(dto.getWarehouseId());
        product.setWarehouse(warehouse);

        return product;
    }
}
