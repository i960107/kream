package com.example.demo.src.user.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByBinAndStatus(String bin, Status status);

    Card findByUserIdxAndDefaultCardAndStatus(Long userIdx, String defaultCard, Status status);

    List<Card> findByUserIdxAndStatus(Long userIdx, Status activated);

    Optional<Card> findByIdxAndUserIdxAndStatus(Long idx, Long userIdx, Status status);
}
