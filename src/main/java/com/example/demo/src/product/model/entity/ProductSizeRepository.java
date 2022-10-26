package com.example.demo.src.product.model.entity;


import com.example.demo.config.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductSizeRepository extends JpaRepository<ProductSize,Long> {

    Optional<ProductSize> findByIdxAndStatus(Long productSizeIdx, Status status);

//    boolean existsByIdxAndStatus(Long productSizeIdx, Status status);

    @Query(name = "select count(ps.idx)>0 from ProductSize ps join fetch Product p where p.idx =:productIdx and ps.idx =:productSizeIdx and ps.status =:status")
    boolean existsByProductIdxAndIdxAndStatus(Long productIdx, Long productSizeIdx, Status status);

    List<ProductSize> findByProductIdxAndStatus(Long productIdx, Status status, Sort sort);

    Boolean existsByIdxAndStatus(Long productIdx, Status status);

    Optional<ProductSize> findByIdxAndProductIdxAndStatus(Long productSizeIdx, Long productIdx, Status activated);
//    @Query("select bs.bidPrice from ProductSize p " +
//            "join fetch BidSale bs " +
//            "where p.productIdx=:productIdx " +
//            "and p.status =:status " +
//            "and bs.bidPrice = max(bs.bidPrice)")
//    Optional<Long> findMaxBidPriceByProductIdxAndStatus(Long productIdx, Status status);
}
