package com.paymong.auth.auth.controller;

import com.paymong.auth.auth.dto.request.LoginReqDto;
import com.paymong.auth.auth.dto.response.LoginResDto;
import com.paymong.auth.auth.dto.response.ReissueResDto;
import com.paymong.auth.auth.service.AuthService;
import com.paymong.core.code.DeviceCode;
import com.paymong.core.exception.failException.InvalidFailException;
import com.paymong.core.response.FailResponse;
import com.paymong.core.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    private ResponseEntity<Object> login(@RequestBody LoginReqDto loginReqDto) {
        LoginResDto loginResDto = authService.login(loginReqDto, DeviceCode.APP);
        return ResponseEntity.ok().body(new SuccessResponse(loginResDto));
    }

    @PostMapping("/login/watch")
    private ResponseEntity<Object> loginWatch(@RequestBody LoginReqDto loginReqDto) {
        LoginResDto loginResDto = authService.login(loginReqDto, DeviceCode.WATCH);
        return ResponseEntity.ok().body(new SuccessResponse(loginResDto));
    }

    @PostMapping("/reissue")
    private ResponseEntity<Object> reissue(@RequestHeader(value = "RefreshToken", required = false) String refreshToken) {
        ReissueResDto reissueResDto = authService.reissue(refreshToken);
        return ResponseEntity.ok().body(new SuccessResponse(reissueResDto));
    }
}
