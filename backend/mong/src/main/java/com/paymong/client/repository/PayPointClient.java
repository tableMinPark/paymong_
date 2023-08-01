package com.paymong.client.repository;

import com.paymong.client.dto.request.RegisterPayPointReqDto;
import com.paymong.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paypoint", configuration = { FeignClientConfig.class })
public interface PayPointClient {
    @PostMapping(value = "/member/paypoint", produces = "application/json")
    ResponseEntity<Object> registerPayPoint(@RequestBody RegisterPayPointReqDto registerPayPointReqDto);
}
