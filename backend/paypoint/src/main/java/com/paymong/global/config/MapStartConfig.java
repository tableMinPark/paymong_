package com.paymong.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MapStartConfig implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("############### Map 스케줄러 시작 ###############");
    }
}
