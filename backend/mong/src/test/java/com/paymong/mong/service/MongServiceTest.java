package com.paymong.mong.service;

import com.paymong.mong.entity.CommonCode;
import com.paymong.mong.entity.Mong;
import com.paymong.mong.repository.CommonCodeRepository;
import com.paymong.mong.repository.MongRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MongServiceTest {
    @Autowired
    private MongRepository mongRepository;
    @Autowired
    private CommonCodeRepository commonCodeRepository;

    @Test
    void registerMongTest() {
        Long memberId = 58L;

        String mongCode = "CH000";
        String name = "별몽";
        LocalTime sleepStart = LocalTime.now();
        LocalTime sleepEnd = LocalTime.now();

        Mong mong = mongRepository.save(Mong.of(memberId, mongCode, name, sleepStart, sleepEnd));

        assertNotNull(mong.getMongId());
    }

    @Test
    void registerMongCodeTest() {
        List<CommonCode> mongCodeList = commonCodeRepository.findByCodeForMongCode(0);
        assertEquals(mongCodeList.size(), 6);
    }

}