package com.paymong.mong.repository;

import com.paymong.mong.entity.MongHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongHistoryRepository extends JpaRepository<MongHistory, Long> {
}
