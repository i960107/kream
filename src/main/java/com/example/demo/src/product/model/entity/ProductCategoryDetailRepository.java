package com.example.demo.src.product.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryDetailRepository extends JpaRepository<ProductCategoryDetail,Long> {

    List<ProductCategoryDetail> findByProductCategoryIdxInAndExposureAndPositionGreaterThanAndStatus(List<Byte> productCategoryIdx, String exposure,byte position, Status status, Sort sort);

    List<ProductCategoryDetail> findByProductCategoryIdxAndExposureAndStatus(byte productCategoryIdx, String exposure, Status status, Sort sort);

}

