package com.example.demo.src.product.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryMapRepository extends JpaRepository<ProductCategoryMap, Long> {
    @Query("SELECT " +
            "m.productCategoryDetail.idx " +
            "FROM " +
            "ProductCategoryMap m " +
            "WHERE " +
            "m.product.idx = :productIdx " +
            "and " +
            "m.status = :mapStatus " +
            "and " +
            "m.product.status= :productStatus")
    List<Long> findProductCategoryDetailIdxListByProductIdxAndStatus(Long productIdx, Status mapStatus, Status productStatus);

    @Query("SELECT m.product from ProductCategoryMap m where m.product.brand.idx = :brandIdx and m.productCategoryDetail.idx in :categoryDetailIdxList and m.status = :status")
    List<Product> findByBrandIdxAndProductCategoryDetailIdxAndStatus(Long brandIdx, List<Long> categoryDetailIdxList, Status status, Pageable pageable);

    List<ProductCategoryMap> findByProductCategoryDetailIdxAndStatus(Long idx, Status activated);
}
