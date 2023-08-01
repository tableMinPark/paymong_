package com.paymong.mong.repository;

import com.paymong.mong.entity.Mong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongRepository extends JpaRepository<Mong, Long> {
}
