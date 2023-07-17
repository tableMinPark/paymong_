package com.paymong.paypoint.repository;

import com.paymong.paypoint.entity.MemberMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberMapRepository extends JpaRepository<MemberMap, Long> {
    Optional<MemberMap> findByMemberId(Long memberId);
}
