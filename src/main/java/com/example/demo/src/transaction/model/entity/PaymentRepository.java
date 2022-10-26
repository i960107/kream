package com.example.demo.src.transaction.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
