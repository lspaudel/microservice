package com.example.core.controller;

import com.example.core.dto.WarehouseRequestDto;
import com.example.core.dto.WarehouseResponseDto;
import com.example.core.entity.Warehouse;
import com.example.core.mapper.WarehouseMapper;
import com.example.core.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    private final WarehouseService service;

    public WarehouseController(WarehouseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<WarehouseResponseDto> create(
            @Valid @RequestBody WarehouseRequestDto dto) {

        Warehouse warehouse = WarehouseMapper.toEntity(dto);
        Warehouse saved = service.create(warehouse);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(WarehouseMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseRequestDto dto) {

        Warehouse warehouse = WarehouseMapper.toEntity(dto);
        Warehouse updated = service.update(id, warehouse);

        return ResponseEntity.ok(WarehouseMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> getById(@PathVariable Long id) {
        Warehouse warehouse = service.getById(id);
        return ResponseEntity.ok(WarehouseMapper.toDto(warehouse));
    }

    @GetMapping
    public ResponseEntity<List<WarehouseResponseDto>> getAll() {
        List<WarehouseResponseDto> list = service.getAll()
                .stream()
                .map(WarehouseMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
