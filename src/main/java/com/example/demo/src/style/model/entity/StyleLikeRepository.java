package com.example.demo.src.style.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StyleLikeRepository extends JpaRepository<StyleLike, Long> {
    Long countByStyleIdxAndStatus(Long idx, Status activated);

    Optional<StyleLike> findByUserIdxAndStyleIdx(Long userIdx, Long styleIdx);

    List<StyleLike> findByStyleIdxAndStatus(Long styleIdx, Status status);
}
