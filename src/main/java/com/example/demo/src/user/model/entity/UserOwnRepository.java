package com.example.demo.src.user.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOwnRepository extends JpaRepository<UserOwn, Long> {
    List<UserOwn> findByUserIdxAndStatus(Long userIdx, Status status);

    Boolean existsByUserIdxAndProductSizeIdxAndStatus(Long userIdx, Long productSizeIdx,Status status);
}

