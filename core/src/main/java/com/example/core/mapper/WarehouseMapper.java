package com.example.core.mapper;

import com.example.core.dto.WarehouseRequestDto;
import com.example.core.dto.WarehouseResponseDto;
import com.example.core.entity.Warehouse;

public class WarehouseMapper {

    public static WarehouseResponseDto toDto(Warehouse warehouse) {
        WarehouseResponseDto dto = new WarehouseResponseDto();
        dto.setId(warehouse.getId());
        dto.setName(warehouse.getName());
        dto.setAddress(warehouse.getAddress());
        return dto;
    }

    public static Warehouse toEntity(WarehouseRequestDto dto) {
        Warehouse warehouse = new Warehouse();
        warehouse.setName(dto.getName());
        warehouse.setAddress(dto.getAddress());
        return warehouse;
    }
}
