package com.paymong.paypoint.repository;

import com.paymong.paypoint.entity.PayPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayPointHistoryRepository extends JpaRepository<PayPointHistory, Long> {
    List<PayPointHistory> findByMemberIdOrderByRegDtDesc(Long memberId);
}
