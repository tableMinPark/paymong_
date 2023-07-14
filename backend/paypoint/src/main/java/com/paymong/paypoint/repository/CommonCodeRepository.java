package com.paymong.paypoint.repository;

import com.paymong.paypoint.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {
    // 멀티 인덱스와 단일 인덱스 적용
    @Query(value =
            "SELECT c.code as foodCode, c.name as name, p.price as price, ph.reg_dt as lastBuy " +
            "FROM common_code c, " +
            "product_code p LEFT OUTER JOIN ( " +
            "   SELECT ph.code, ph.reg_dt " +
            "   FROM pay_point_history ph, product_code p" +
            "   WHERE ph.member_id = :memberId " +
            "   AND ph.code = p.code " +
            "   ORDER BY ph.reg_dt DESC " +
            "   LIMIT 1 " +
            ") ph " +
            "ON p.code = ph.code " +
            "WHERE p.code = c.code " +
            "AND c.group_code = :category ", nativeQuery = true)
    List<FoodAndSnackMapping> findFoodAndSnack(Long memberId, String category);
}
