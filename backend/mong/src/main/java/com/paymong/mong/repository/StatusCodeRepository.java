package com.paymong.mong.repository;

import com.paymong.mong.entity.StatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusCodeRepository extends JpaRepository<StatusCode, Long> {
    Optional<StatusCode> findByCode(String code);
}
