package com.paymong.lifecycle.repository;

import com.paymong.lifecycle.entity.Mong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongRepository extends JpaRepository<Mong, Long> {
}
