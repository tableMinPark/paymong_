package com.paymong.collect.repository;

import com.paymong.collect.entity.MapCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MapCollectRepository extends JpaRepository<MapCollect, Long> {
    @Query(value =
            "SELECT m.member_id as memberId, c.code, c.name " +
            "FROM (" +
            "   SELECT * FROM common_code " +
            "   WHERE code like \"MP%\") c " +
            "LEFT OUTER JOIN ( " +
            "   SELECT * FROM map_collect " +
            "   WHERE member_id = :memberId) m " +
            "ON m.map_code = c.code", nativeQuery = true)
    // native 쿼리를 쓰지 않으면 where 절에 sub query 를 사용할 수 없다.
    List<MapCollectMapping> findByMemberId(Long memberId);

    Optional<MapCollect> findByMemberIdAndMapCode(Long memberId, String mapCode);
}

