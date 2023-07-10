package com.paymong.auth.repository;

import com.paymong.auth.entity.PayPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayPointRepository extends JpaRepository<PayPoint, Long> {
}
