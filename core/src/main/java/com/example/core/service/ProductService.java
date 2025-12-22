package com.example.core.service;

import com.example.core.entity.Product;

import java.util.List;

public interface ProductService {

    Product create(Product product, Long warehouseId);
    Product update(Long id, Product product);
    void delete(Long id);
    Product getById(Long id);
    List<Product> getAll();
}
