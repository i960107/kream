package com.example.demo.src.user.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndStatus(String email, Status status);

    Optional<User> findByPhoneAndStatus(String phone, Status status);

    Optional<User> findByIdxAndStatus(Long userIdx,Status status);

    boolean existsByPhone(String phone);

    Optional<User> findByPhoneAndEmailAndStatus(String phone, String email, Status activated);

}
