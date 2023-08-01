package com.paymong.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/admin")
public class AdminController {


    @GetMapping("/log")
    private ResponseEntity<Object> findLoginLog() {
        log.info("findLoginLog - Call");
        return ResponseEntity.ok().body(null);
    }
}
