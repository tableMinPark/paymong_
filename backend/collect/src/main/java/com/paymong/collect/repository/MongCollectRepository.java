package com.paymong.collect.repository;

import com.paymong.collect.entity.MongCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongCollectRepository extends JpaRepository<MongCollect, Long> {
    @Query(value =
            "SELECT m.member_id as memberId, c.code, c.name " +
            "FROM (" +
            "   SELECT * FROM common_code " +
            "   WHERE code like \"CH%\") c " +
            "LEFT OUTER JOIN ( " +
            "   SELECT * FROM mong_collect " +
            "   WHERE member_id = :memberId) m " +
            "ON m.mong_code = c.code", nativeQuery = true)
    // native 쿼리를 쓰지 않으면 where 절에 sub query 를 사용할 수 없다.
    List<MongCollectMapping> findByMemberId(Long memberId);

    Optional<MongCollect> findByMemberIdAndMongCode(Long memberId, String mongCode);
}
