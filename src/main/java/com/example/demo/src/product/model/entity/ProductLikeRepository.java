package com.example.demo.src.product.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {

    @Query("SELECT count(p) FROM ProductLike p WHERE p.productSize.idx in :productSizeIdxList and p.status = :status")
    Long findCountByProductIdxInAndStatus(List<Long> productSizeIdxList , Status status);

    Optional<ProductLike> findByProductSizeIdxAndUserIdxAndStatus(Long productSizeIdx, Long userIdx, Status status);

    Optional<ProductLike> findByProductSizeIdxAndUserIdx(Long productSizeIdx, Long userIdx);

    List<ProductLike> findByUserIdxAndStatus(Long userIdx, Status status);
    List<ProductLike> findByUserIdxAndStatus(Long userIdx, Status status, Sort sort);
}
