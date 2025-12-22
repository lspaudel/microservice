package com.example.core.service.impl;

import com.example.core.entity.Product;
import com.example.core.entity.Warehouse;
import com.example.core.exception.ResourceNotFoundException;
import com.example.core.repository.ProductRepository;
import com.example.core.repository.WarehouseRepository;
import com.example.core.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final WarehouseRepository warehouseRepo;

    public ProductServiceImpl(ProductRepository productRepo,
                              WarehouseRepository warehouseRepo) {
        this.productRepo = productRepo;
        this.warehouseRepo = warehouseRepo;
    }


    @Override
    public Product create(Product product, Long warehouseId) {
        Warehouse warehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        product.setWarehouse(warehouse);
        return productRepo.save(product);
    }

    @Override
    public Product update(Long id, Product updated) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        existing.setName(updated.getName());
        existing.setSku(updated.getSku());
        existing.setPrice(updated.getPrice());
        existing.setQuantity(updated.getQuantity());

        return productRepo.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!productRepo.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepo.deleteById(id);
    }

    @Override
    public Product getById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAll() {
        return productRepo.findAll();
    }
}
