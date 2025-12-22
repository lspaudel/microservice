package com.example.core.repository;

import com.example.core.entity.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long> {
}
