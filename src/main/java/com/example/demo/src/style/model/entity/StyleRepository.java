package com.example.demo.src.style.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StyleRepository extends JpaRepository<Style, Long> {
    Page<Style> findAllByStatus(Status activated, Pageable pageable);

    Optional<Style> findByIdxAndStatus(Long styleIdx, Status activated);
}
