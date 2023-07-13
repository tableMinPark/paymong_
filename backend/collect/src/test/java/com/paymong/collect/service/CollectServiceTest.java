package com.paymong.collect.service;

import com.paymong.collect.repository.MapCollectMapping;
import com.paymong.collect.repository.MapCollectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CollectServiceTest {
    @Autowired
    private MapCollectRepository mapCollectRepository;

    @Test
    void findMapTest() {
        List<MapCollectMapping> mapCollectList = mapCollectRepository.findByMemberId(58L);
        assertEquals(mapCollectList.size(), 44);
    }

}