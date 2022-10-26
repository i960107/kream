package com.example.demo.src.style.model.entity;

import com.example.demo.config.Status;
import com.example.demo.src.style.StyleProvider;
import com.example.demo.src.style.model.dto.GetStyleProductRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StyleProductRepository extends JpaRepository<StyleProduct, Long> {

    List<StyleProduct> findByStyleIdxAndStatus(Long idx, Status status);
}
