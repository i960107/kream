package com.example.demo.src.style.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StyleHashTagRepository extends JpaRepository<StyleHashTag, Long> {


    Page<StyleHashTag> findByHashtagIdxAndStatus(Long hashtagIdx, Status activated, Pageable pageable);
}
