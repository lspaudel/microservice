package com.example.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WarehouseRequestDto {

    @NotBlank(message = "Warehouse name must not be blank")
    @Size(max = 100, message = "Warehouse name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Address must not be blank")
    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;
}
