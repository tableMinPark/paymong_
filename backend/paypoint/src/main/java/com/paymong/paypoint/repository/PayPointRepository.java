package com.paymong.paypoint.repository;

import com.paymong.paypoint.entity.PayPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayPointRepository extends JpaRepository<PayPoint, Long> {
    Optional<PayPoint> findByMemberId(Long memberId);
}
