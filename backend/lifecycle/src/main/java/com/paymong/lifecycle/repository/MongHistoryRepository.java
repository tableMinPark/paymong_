package com.paymong.lifecycle.repository;

import com.paymong.lifecycle.entity.MongHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongHistoryRepository extends JpaRepository<MongHistory, Long> {
}
