package com.paymong.client.repository;

import com.paymong.client.dto.request.*;
import com.paymong.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "lifecycle", configuration = { FeignClientConfig.class })
public interface LifeCycleClient {
    @PostMapping(value = "/lifecycle/start", produces = "application/json")
    ResponseEntity<Object> startScheduler(@RequestBody StartSchedulerReqDto startSchedulerReqDto);

    @PutMapping(value = "/lifecycle/pause", produces = "application/json")
    ResponseEntity<Object> pauseScheduler(@RequestBody PauseSchedulerReqDto pauseSchedulerReqDto);

    @PutMapping(value = "/lifecycle/restart", produces = "application/json")
    ResponseEntity<Object> restartScheduler(@RequestBody RestartSchedulerReqDto restartSchedulerReqDto);

    @DeleteMapping(value = "/lifecycle/stop", produces = "application/json")
    ResponseEntity<Object> stopScheduler(@RequestBody StopSchedulerReqDto stopSchedulerReqDto);

    @PostMapping(value = "/lifecycle/register", produces = "application/json")
    ResponseEntity<Object> registerMongScheduler(@RequestBody RegisterMongSchedulerReqDto registerMongSchedulerReqDto);

    @PostMapping(value = "/lifecycle/evolution", produces = "application/json")
    ResponseEntity<Object> nextLevelScheduler(@RequestBody NextLevelSchedulerReqDto nextLevelSchedulerReqDto);

}
