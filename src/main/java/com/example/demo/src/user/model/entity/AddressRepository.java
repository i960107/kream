package com.example.demo.src.user.model.entity;

import com.example.demo.config.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findByIdxAndStatus(Long idx, Status status);

    List<Address> findByUserIdxAndStatus(Long userIdx, Status activated);

    Address findByUserIdxAndDefaultAddressAndStatus(Long userIdx, String defaultAddress, Status status);

    Optional<Address> findByIdxAndUserIdxAndStatus(Long addressIdx, Long userIdx, Status status);
}
