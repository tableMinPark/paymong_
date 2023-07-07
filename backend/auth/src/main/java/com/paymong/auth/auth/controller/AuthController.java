package com.paymong.auth.auth.controller;

import com.paymong.auth.auth.dto.request.LoginReqDto;
import com.paymong.auth.auth.dto.response.LoginResDto;
import com.paymong.auth.auth.dto.response.ReissueResDto;
import com.paymong.auth.auth.service.AuthService;
import com.paymong.core.code.ErrorCode;
import com.paymong.core.exception.UnAuthException;
import com.paymong.core.response.ErrorResponse;
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
    public ResponseEntity<Object> login(@RequestBody LoginReqDto loginReqDto) {

        log.info("login - Call");

        try {
            LoginResDto loginResDto = authService.login(loginReqDto);
            return ResponseEntity.ok().body(new SuccessResponse(loginResDto));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ErrorResponse(ErrorCode.INTERNAL_SERVER));
        }
    }

    @PostMapping("/login/watch")
    public ResponseEntity<Object> loginWatch(@RequestBody LoginReqDto loginReqDto) {

        log.info("loginWatch - Call");

        try {
            LoginResDto loginResDto = authService.login(loginReqDto);
            return ResponseEntity.ok().body(new SuccessResponse(loginResDto));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ErrorResponse(ErrorCode.INTERNAL_SERVER));
        }
    }

    @PostMapping("/reissue")
    private ResponseEntity<Object> reissue(@RequestHeader(value = "RefreshToken") String refreshToken) {

        log.info("reissue - Call");

        try {
            ReissueResDto reissueResDto = authService.reissue(refreshToken);
            return ResponseEntity.ok().body(new SuccessResponse(reissueResDto));
        } catch (UnAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new FailResponse(e.getFailCode()));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse(ErrorCode.INTERNAL_SERVER));
        }
    }
}
