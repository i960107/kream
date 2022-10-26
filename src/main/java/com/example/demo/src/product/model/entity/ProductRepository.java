package com.example.demo.src.product.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatus(Status status);

    Optional<Product> findByIdxAndStatus(Long productIdx, Status status);

    Optional<Product> findByIdxAndStatus(Long productIdx, Status status, Sort sort);

    Boolean existsByIdxAndStatus(Long productIdx, Status status);

    @Query("Select p FROM Product p where p.brand.idx = :brandIdx and p.status = :status")
    List<Product> findByBrandIdxAndStatus(Long brandIdx, Status status);

//    @Query("select p From Product p where p.brand.idx = :brandIdx and p.categoryList.productCategoryDetail.idx in :categoryDetailIdxList and p.status = :productStatus")
//    List<Product> findByBrandIdxAndProductCategoryDetailIdxAndStatus(Long brandIdx, List<Long> categoryDetailIdxList, Status productStatus);
}
