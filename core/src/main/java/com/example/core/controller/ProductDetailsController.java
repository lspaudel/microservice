package com.example.core.controller;

import com.example.core.dto.ProductDetailsRequestDto;
import com.example.core.dto.ProductDetailsResponseDto;
import com.example.core.entity.ProductDetails;
import com.example.core.mapper.ProductDetailsMapper;
import com.example.core.service.ProductDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product-details")
public class ProductDetailsController {

    private final ProductDetailsService service;

    public ProductDetailsController(ProductDetailsService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductDetailsResponseDto> create(
            @Valid @RequestBody ProductDetailsRequestDto dto) {
        ProductDetails details = ProductDetailsMapper.toEntity(dto);
        ProductDetails saved = service.create(details);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductDetailsMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailsResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductDetailsRequestDto dto) {
        ProductDetails details = ProductDetailsMapper.toEntity(dto);
        ProductDetails updated = service.update(id, details);
        return ResponseEntity.ok(ProductDetailsMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsResponseDto> getById(@PathVariable Long id) {
        ProductDetails details = service.getById(id);
        return ResponseEntity.ok(ProductDetailsMapper.toDto(details));
    }

    @GetMapping
    public ResponseEntity<List<ProductDetailsResponseDto>> getAll() {
        List<ProductDetailsResponseDto> list = service.getAll()
                .stream()
                .map(ProductDetailsMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
