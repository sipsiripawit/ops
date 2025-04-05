package com.yipintsoi.inventoryservice.repository;

import com.yipintsoi.inventoryservice.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
