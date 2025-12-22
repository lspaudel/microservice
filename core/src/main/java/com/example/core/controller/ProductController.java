package com.example.core.controller;

import com.example.core.dto.ProductRequestDto;
import com.example.core.dto.ProductResponseDto;
import com.example.core.entity.Product;
import com.example.core.mapper.ProductMapper;
import com.example.core.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(
            @Valid @RequestBody ProductRequestDto dto) {

        Product product = ProductMapper.toEntity(dto);
        Product saved = service.create(product, dto.getWarehouseId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductMapper.toDto(saved));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto dto) {
        Product product = ProductMapper.toEntity(dto);
        Product updated = service.update(id, product);
        return ResponseEntity.ok(ProductMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id) {
        Product product = service.getById(id);
        return ResponseEntity.ok(ProductMapper.toDto(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAll() {
        List<ProductResponseDto> list = service.getAll()
                .stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
