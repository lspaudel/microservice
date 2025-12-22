package com.example.core.service;

import com.example.core.entity.Warehouse;

import java.util.List;

public interface WarehouseService {
    Warehouse create(Warehouse warehouse);
    Warehouse update(Long id, Warehouse warehouse);
    void delete(Long id);
    Warehouse getById(Long id);
    List<Warehouse> getAll();
}
