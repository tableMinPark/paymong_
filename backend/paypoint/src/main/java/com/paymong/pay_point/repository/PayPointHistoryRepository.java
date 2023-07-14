package com.paymong.pay_point.repository;

import com.paymong.pay_point.entity.PayPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayPointHistoryRepository extends JpaRepository<PayPointHistory, Long> {
    List<PayPointHistory> findByMemberIdOrderByRegDtDesc(Long memberId);
}
