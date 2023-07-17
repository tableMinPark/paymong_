package com.paymong.lifecycle.controller;

import com.paymong.global.response.SuccessResponse;
import com.paymong.lifecycle.dto.request.*;
import com.paymong.lifecycle.service.LifeCycleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/lifecycle")
public class LifeCycleController {
    private final LifeCycleService lifeCycleService;

    @PostMapping("/start")
    private ResponseEntity<Object> startScheduler(@RequestBody StartSchedulerReqDto startSchedulerReqDto) {
        lifeCycleService.startScheduler(startSchedulerReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(null));
    }

    @PutMapping("/pause")
    private ResponseEntity<Object> pauseScheduler(@RequestBody PauseSchedulerReqDto pauseSchedulerReqDto) {
        lifeCycleService.pauseScheduler(pauseSchedulerReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(null));
    }

    @PutMapping("/restart")
    private ResponseEntity<Object> restartScheduler(@RequestBody RestartSchedulerReqDto restartSchedulerReqDto) {
        lifeCycleService.restartScheduler(restartSchedulerReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(null));
    }

    @DeleteMapping("/stop")
    private ResponseEntity<Object> stopScheduler(@RequestBody StopSchedulerReqDto stopSchedulerReqDto) {
        lifeCycleService.stopScheduler(stopSchedulerReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(null));
    }

    @PostMapping("/register")
    private ResponseEntity<Object> registerMongScheduler(@RequestBody RegisterMongSchedulerReqDto registerMongSchedulerReqDto) {
        lifeCycleService.registerMongScheduler(registerMongSchedulerReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(null));
    }

    @PostMapping("/evolution")
    private ResponseEntity<Object> nextLevelScheduler(@RequestBody NextLevelSchedulerReqDto nextLevelSchedulerReqDto) {
        lifeCycleService.nextLevelScheduler(nextLevelSchedulerReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(null));
    }
}
