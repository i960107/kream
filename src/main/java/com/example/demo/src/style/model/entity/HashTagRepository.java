package com.example.demo.src.style.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    @Query("SELECT h.idx FROM HashTag h WHERE name = :hashTag and status = :status")
    Long findByNameAndStatus(String hashTag, Status status);
}
