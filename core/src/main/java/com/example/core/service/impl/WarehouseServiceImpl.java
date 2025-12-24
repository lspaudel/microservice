package com.example.core.service.impl;

import com.example.core.entity.Warehouse;
import com.example.shared.exception.ResourceNotFoundException;
import com.example.core.repository.WarehouseRepository;
import com.example.core.service.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository repo;

    public WarehouseServiceImpl(WarehouseRepository repo) {
        this.repo = repo;
    }

    @Override
    public Warehouse create(Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse must not be null");
        }
        return repo.save(warehouse);
    }

    @Override
    public Warehouse update(Long id, Warehouse warehouse) {
        Warehouse existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse with id " + id + " not found"));
        existing.setName(warehouse.getName());
        existing.setAddress(warehouse.getAddress());
        return repo.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Warehouse with id " + id + " not found");
        }
        repo.deleteById(id);
    }

    @Override
    public Warehouse getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse with id " + id + " not found"));
    }

    @Override
    public List<Warehouse> getAll() {
        return repo.findAll();
    }
}
