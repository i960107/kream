package com.example.demo.src.product.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Query("SELECT " +
            "i.image " +
            "FROM " +
            "ProductImage i " +
            "WHERE " +
            "i.product.idx = :productIdx " +
            "and i.position <= :position " +
            "and i.status = :status")
    String findByProductIdxPositionLessThanAndStatus(Long productIdx, byte position, Status status);
}
