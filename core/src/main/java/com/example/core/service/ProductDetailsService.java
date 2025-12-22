package com.example.core.service;

import com.example.core.entity.ProductDetails;

import java.util.List;

public interface ProductDetailsService {
    ProductDetails create(ProductDetails details);
    ProductDetails update(Long id, ProductDetails details);
    void delete(Long id);
    ProductDetails getById(Long id);
    List<ProductDetails> getAll();
}
