package com.paymong.gateway.repository;

import com.paymong.gateway.entity.Mong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongRepository extends JpaRepository<Mong, Long> {
    // 오류로 인해 두 개의 몽이 조회되는 경우 LIMIT 를 이용해서 가장 최근에 생성된 몽을 가져오도록 함
    @Query(value =
            "SELECT * " +
                    "FROM mong m " +
                    "WHERE m.member_id = :memberId " +
                    "AND m.active = true " +
                    "ORDER BY m.mong_id DESC " +
                    "LIMIT 1", nativeQuery = true)
    Optional<Mong> findByMemberId(Long memberId);
}
