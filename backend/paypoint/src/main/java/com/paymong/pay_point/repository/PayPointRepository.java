package com.paymong.pay_point.repository;

import com.paymong.pay_point.entity.PayPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayPointRepository extends JpaRepository<PayPoint, Long> {
    Optional<PayPoint> findByMemberId(Long memberId);
}
