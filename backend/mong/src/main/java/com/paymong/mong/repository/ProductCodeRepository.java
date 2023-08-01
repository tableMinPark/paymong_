package com.paymong.mong.repository;

import com.paymong.mong.entity.ProductCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCodeRepository extends JpaRepository<ProductCode, Long> {
    Optional<ProductCode> findByCode(String code);
}
