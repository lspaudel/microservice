package com.example.core.service.impl;

import com.example.core.entity.Product;
import com.example.core.entity.Warehouse;
import com.example.core.events.ProductEventPublisher;
import com.example.core.repository.ProductRepository;
import com.example.core.repository.WarehouseRepository;
import com.example.core.service.ProductService;
import com.example.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final WarehouseRepository warehouseRepo;
    private final ProductEventPublisher eventPublisher;

    public ProductServiceImpl(ProductRepository productRepo,
            WarehouseRepository warehouseRepo,
            ProductEventPublisher eventPublisher) {
        this.productRepo = productRepo;
        this.warehouseRepo = warehouseRepo;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Product create(Product product, Long warehouseId) {
        Warehouse warehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        product.setWarehouse(warehouse);
        Product savedProduct = productRepo.save(product);

        // Publish event asynchronously
        eventPublisher.publishProductCreated(savedProduct);
        log.info("Product created: ID={}, Name={}", savedProduct.getId(), savedProduct.getName());

        return savedProduct;
    }

    @Override
    @CachePut(value = "products", key = "#id")
    public Product update(Long id, Product updated) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        existing.setName(updated.getName());
        existing.setSku(updated.getSku());
        existing.setPrice(updated.getPrice());
        existing.setQuantity(updated.getQuantity());

        Product savedProduct = productRepo.save(existing);
        log.info("Product updated in cache: ID={}", id);
        return savedProduct;
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public void delete(Long id) {
        if (!productRepo.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepo.deleteById(id);
        log.info("Product deleted from cache: ID={}", id);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public Product getById(Long id) {
        log.info("Fetching product from database: ID={}", id);
        return productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAll() {
        return productRepo.findAll();
    }
}
