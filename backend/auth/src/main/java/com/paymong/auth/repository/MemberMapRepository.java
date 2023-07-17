package com.paymong.auth.repository;

import com.paymong.auth.entity.MemberMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberMapRepository extends JpaRepository<MemberMap, Long> {
}
