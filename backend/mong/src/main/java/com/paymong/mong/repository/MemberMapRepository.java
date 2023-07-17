package com.paymong.mong.repository;

import com.paymong.mong.entity.MemberMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberMapRepository extends JpaRepository<MemberMap, Long> {
    Optional<MemberMap> findByMemberId(Long memberId);
}
