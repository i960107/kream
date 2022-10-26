package com.example.demo.src.bid.sale.model.entity;


import com.example.demo.config.Status;
import com.example.demo.src.bid.sale.model.dto.GetBidSaleRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BidSaleRepository extends JpaRepository<BidSale, Long> {
    @Query(value = "SELECT " +
            "min(b.bidPrice) " +
            "FROM " +
            "BidSale b " +
            "JOIN " +
            "b.productSize ps " +
            "WHERE " +
            "ps.idx in :productSizeIdx and b.status = :status")
    Long findMinBidPrice(@Param("productSizeIdx") List<Long> productSizeIdx, @Param("status") Status status);


    @Query(value = "SELECT " +
            "min(b.bidPrice) " +
            "FROM " +
            "BidSale b " +
            "JOIN " +
            "b.productSize ps " +
            "WHERE " +
            "ps.idx = :productSizeIdx and b.status = :status")
    Long findMinBidPrice(Long productSizeIdx, @Param("status") Status status);


    BidSale findFirstByBidPriceAndProductSizeIdxAndStatus(Long bidPrice,Long productSizeIdx, Status status, Sort sort);

    @Query(value = "SELECT " +
            "new com.example.demo.src.bid.sale.model.dto.GetBidSaleRes(p.size, b.bidPrice, count(b)) " +
            "FROM " +
            "BidSale b " +
            "JOIN " +
            "b.productSize p " +
            "WHERE " +
            "b.status = :status and b.productSize.idx in :productSizeList " +
            "GROUP BY " +
            "b.productSize, b.bidPrice")
    Page<GetBidSaleRes> findByProductSizeIdxInAndStatus(List<Long> productSizeList, Status status, Pageable pageable);

    @Query(value = "SELECT " +
            "new com.example.demo.src.bid.sale.model.dto.GetBidSaleRes(p.size, b.bidPrice, count(b)) " +
            "FROM " +
            "BidSale b " +
            "JOIN " +
            "b.productSize p " +
            "WHERE " +
            "b.status = :status and b.productSize.idx = :productSizeIdx " +
            "GROUP BY " +
            "b.productSize, b.bidPrice")
    Page<GetBidSaleRes> findByProductSizeIdxAndStatus(Long productSizeIdx, Status status, Pageable pageable);

    List<BidSale> findByUserIdxAndStatus(Long userIdx, Status status, Pageable pageable);

    Optional<BidSale> findByIdxAndProductSizeIdxAndStatus(Long bidSaleIdx, Long productSizeIdx, Status bidding);

    Optional<BidSale> findByIdxAndStatus(Long bidSaleIdx, Status status);
}
