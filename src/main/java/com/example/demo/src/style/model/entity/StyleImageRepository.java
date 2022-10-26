package com.example.demo.src.style.model.entity;

import com.example.demo.config.Status;
import org.apache.xerces.impl.XMLEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StyleImageRepository extends JpaRepository<StyleImage, Long > {
    List<StyleImage> findByStyleIdxAndStatus(Long uploadedStyleIdx, Status activated);


}
