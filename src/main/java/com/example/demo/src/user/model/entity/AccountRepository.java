package com.example.demo.src.user.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUserIdxAndStatus(Long  userIdx, Status status);

    Optional<Account> findByIdxAndUserIdxAndStatus(Long idx, Long userIdx, Status status);
}
