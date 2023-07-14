package com.paymong.mong.repository;

import com.paymong.mong.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonCodeRepository extends JpaRepository<CommonCode, String> {
    @Query(value =
            "SELECT * " +
            "FROM common_code " +
            "WHERE code LIKE CONCAT(\"CH\", :level, \"%\") ", nativeQuery = true)
    List<CommonCode> findByCodeForMongCode(Integer level);
}
