package com.example.demo.src.product.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    List<ProductCategory> findByPositionGreaterThanEqualAndStatus(Byte position, Status status, Sort sort);

    List<ProductCategory> findByPositionLessThanAndStatus(byte position, Status status);
}
