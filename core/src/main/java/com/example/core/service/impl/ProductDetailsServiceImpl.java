package com.example.core.service.impl;

import com.example.core.entity.Product;
import com.example.core.entity.ProductDetails;
import com.example.shared.exception.ResourceNotFoundException;
import com.example.core.repository.ProductDetailsRepository;
import com.example.core.repository.ProductRepository;
import com.example.core.service.ProductDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductDetailsServiceImpl implements ProductDetailsService {

    private final ProductDetailsRepository productDetailsRepo;
    private final ProductRepository productRepo;

    public ProductDetailsServiceImpl(ProductDetailsRepository productDetailsRepo,
                                     ProductRepository productRepo) {
        this.productDetailsRepo = productDetailsRepo;
        this.productRepo = productRepo;
    }

    @Override
    public ProductDetails create(ProductDetails details) {

        Long productId = details.getProduct().getId();

        Product product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id " + productId)
                );

        details.setProduct(product);
        return productDetailsRepo.save(details);
    }

    @Override
    public ProductDetails update(Long id, ProductDetails updated) {

        ProductDetails existing = productDetailsRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ProductDetails not found with id " + id)
                );

        existing.setDescription(updated.getDescription());
        existing.setManufacturer(updated.getManufacturer());
        existing.setWeight(updated.getWeight());
        existing.setColor(updated.getColor());

        return productDetailsRepo.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!productDetailsRepo.existsById(id)) {
            throw new ResourceNotFoundException("ProductDetails not found with id " + id);
        }
        productDetailsRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetails getById(Long id) {
        return productDetailsRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ProductDetails not found with id " + id)
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDetails> getAll() {
        return productDetailsRepo.findAll();
    }
}
