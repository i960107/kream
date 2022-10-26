package com.example.demo.src.bid.purchase.model.entity;

import com.example.demo.config.Status;
import com.example.demo.src.bid.purchase.model.dto.GetBidPurchaseRes;
import com.example.demo.src.bid.sale.model.dto.GetBidSaleRes;
import com.example.demo.src.bid.sale.model.entity.BidSale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidPurchaseRepository extends JpaRepository<BidPurchase,Long> {

    @Query(value = "SELECT " +
            "max(b.bidPrice) " +
            "FROM " +
            "BidPurchase b " +
            "JOIN " +
            "b.productSize ps " +
            "WHERE " +
            "ps.idx in :productSizeIdx and b.status = :status")
    Long findMaxBidPrice(@Param("productSizeIdx") List<Long> productSizeIdx, @Param("status") Status status);

    @Query(value = "SELECT " +
            "max(b.bidPrice) " +
            "FROM " +
            "BidPurchase b " +
            "JOIN " +
            "b.productSize ps " +
            "WHERE " +
            "ps.idx = :productSizeIdx and b.status = :status")
    Long findMaxBidPrice(@Param("productSizeIdx") Long productSizeIdx, @Param("status") Status status);

    @Query(value = "SELECT " +
            "new com.example.demo.src.bid.purchase.model.dto.GetBidPurchaseRes(p.size, b.bidPrice, count(b)) " +
            "FROM " +
            "BidPurchase b " +
            "JOIN " +
            "b.productSize p " +
            "WHERE " +
            "b.status = :status and b.productSize.idx in :productSizeList " +
            "GROUP BY " +
            "b.productSize, b.bidPrice")
    Page<GetBidPurchaseRes> findByProductSizeIdxInAndStatus(List<Long> productSizeList, Status status, Pageable pageable);

    @Query(value = "SELECT " +
            "new com.example.demo.src.bid.purchase.model.dto.GetBidPurchaseRes(p.size, b.bidPrice, count(b)) " +
            "FROM " +
            "BidPurchase b " +
            "JOIN " +
            "b.productSize p " +
            "WHERE " +
            "b.status = :status and b.productSize.idx = :productSizeIdx " +
            "GROUP BY " +
            "b.productSize, b.bidPrice")
    Page<GetBidPurchaseRes> findByProductSizeIdxAndStatus(Long productSizeIdx, Status status, Pageable pageable);

    BidPurchase findFirstByBidPriceAndProductSizeIdxAndStatus(Long bidPrice, Long productSizeIdx, Status bidding, Sort sort);

    List<BidPurchase> findByUserIdxAndStatus(Long userIdx, Status status, Pageable pageable);

    Optional<BidPurchase> findByIdxAndUserIdxAndAndStatus(Long bidPurchaseIdx, Long userIdx, Status registering);

    Optional<BidPurchase> findByIdxAndStatus(Long bidPurchaseIdx, Status status);

    Optional<BidPurchase> findByIdxAndCardIdxAndTotalPriceAndStatus(Long bidPurchaseIdx, Long cardIdx, Long totalPrice, Status status);
}
