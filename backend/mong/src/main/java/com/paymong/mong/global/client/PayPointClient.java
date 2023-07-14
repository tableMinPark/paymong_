package com.paymong.mong.global.client;

import com.paymong.core.response.BasicResponse;
import com.paymong.mong.dto.request.RegisterPayPointReqDto;
import com.paymong.mong.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paypoint", configuration = { FeignClientConfig.class })
public interface PayPointClient {
    @PostMapping(value = "/member/paypoint", produces = "application/json")
    ResponseEntity<Object> registerPayPoint(@RequestBody RegisterPayPointReqDto registerPayPointReqDto);
}
