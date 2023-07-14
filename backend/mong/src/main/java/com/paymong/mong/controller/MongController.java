package com.paymong.mong.controller;

import com.paymong.global.exception.fail.InvalidFailException;
import com.paymong.global.response.SuccessResponse;
import com.paymong.mong.dto.request.RegisterMongReqDto;
import com.paymong.mong.dto.response.FindMongInfoResDto;
import com.paymong.mong.dto.response.FindMongResDto;
import com.paymong.mong.dto.response.FindMongStatusResDto;
import com.paymong.global.code.MongFailCode;
import com.paymong.mong.service.MongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// mong Id 를 통해 조회하는 API 를 관리하는 컨트롤러
// 기존 v1 앱의 매핑 때문에 안드로이드 버전 업데이트 이후 매핑 수정 에정 (이전 버전 서비스 유지를 위함)
@Slf4j
@RestController
@RequiredArgsConstructor
public class MongController {
    private final MongService mongService;

    @GetMapping("/mong")
    private ResponseEntity<Object> findMong() {
        FindMongResDto findMongResDto = mongService.findMong();
        return ResponseEntity.ok().body(new SuccessResponse(findMongResDto));
    }

    @GetMapping("/mong/info")
    private ResponseEntity<Object> findMongInfo() {
        FindMongInfoResDto findMongInfoResDto = mongService.findMongInfo();
        return ResponseEntity.ok().body(new SuccessResponse(findMongInfoResDto));
    }

    @GetMapping("/mong/status")
    private ResponseEntity<Object> findMongStatus() {
        FindMongStatusResDto findMongStatusResDto = mongService.findMongStatus();
        return ResponseEntity.ok().body(new SuccessResponse(findMongStatusResDto));
    }

    @PostMapping("/management")
    private ResponseEntity<Object> registerMong(@RequestBody RegisterMongReqDto registerMongReqDto) {
        if (registerMongReqDto.getSleepStart() == null || registerMongReqDto.getSleepEnd() == null)
            throw new InvalidFailException(MongFailCode.INVALID_INVALID_REQUEST_DTO);

        mongService.registerMong(registerMongReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(null));
    }

    @DeleteMapping("/mong")
    private ResponseEntity<Object> deleteMong() {
        mongService.deleteMong();
        return ResponseEntity.ok().body(new SuccessResponse(null));
    }
}
