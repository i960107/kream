package com.example.demo.src.transaction.model.entity;

import com.example.demo.config.Status;
import com.example.demo.src.transaction.model.dto.WinningBidPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findTop2ByProductSizeIdxInAndStatus(List<Long> productSizeList, Status status, Sort sort);
    List<Transaction> findTop1ByProductSizeIdxInAndStatus(List<Long> productSizeList, Status status, Sort sort);

    WinningBidPrice findTop1ByProductSizeIdxAndStatus(Long productSizeIdx, Status status, Sort sort);

    Page<Transaction> findByProductSizeIdxInAndCreatedAtGreaterThanEqualAndStatus(List<Long> productSizeList, LocalDateTime referenceDate, Status Status, Pageable pageable);
    Page<Transaction> findByProductSizeIdxInAndStatus(List<Long> productSizeList, Status Status, Pageable pageable);

    Page<Transaction> findByProductSizeIdxAndCreatedAtGreaterThanEqualAndStatus(Long productSizeIdx, LocalDateTime referenceDate, Status status, Pageable pageable);
    Page<Transaction> findByProductSizeIdxAndStatus(Long productSizeIdx, Status status, Pageable pageable);

    Optional<Transaction> findByIdxAndStatus(Long transactionIdx, Status status);
}
