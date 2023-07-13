package com.paymong.mong.controller;

import com.paymong.core.exception.fail.InvalidFailException;
import com.paymong.core.response.SuccessResponse;
import com.paymong.mong.dto.request.RegisterMongReqDto;
import com.paymong.mong.dto.response.FindMongInfoResDto;
import com.paymong.mong.dto.response.FindMongResDto;
import com.paymong.mong.dto.response.FindMongStatusResDto;
import com.paymong.mong.global.code.MongFailCode;
import com.paymong.mong.service.MongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// mong Id 를 통해 조회하는 API 를 관리하는 컨트롤러
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mong")
public class MongController {
    private final MongService mongService;

    @GetMapping("")
    private ResponseEntity<Object> findMong() {
        FindMongResDto findMongResDto = mongService.findMong();
        return ResponseEntity.ok().body(new SuccessResponse(findMongResDto));
    }

    @GetMapping("/info")
    private ResponseEntity<Object> findMongInfo() {
        FindMongInfoResDto findMongInfoResDto = mongService.findMongInfo();
        return ResponseEntity.ok().body(new SuccessResponse(findMongInfoResDto));
    }

    @GetMapping("/status")
    private ResponseEntity<Object> findMongStatus() {
        FindMongStatusResDto findMongStatusResDto = mongService.findMongStatus();
        return ResponseEntity.ok().body(new SuccessResponse(findMongStatusResDto));
    }

    @PostMapping("")
    private ResponseEntity<Object> registerMong(@RequestBody RegisterMongReqDto registerMongReqDto) {
        if (registerMongReqDto.getSleepStart() == null || registerMongReqDto.getSleepEnd() == null)
            throw new InvalidFailException(MongFailCode.INVALID_INVALID_REQUEST_DTO);

        mongService.registerMong(registerMongReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(null));
    }

    @DeleteMapping("")
    private ResponseEntity<Object> deleteMong() {
        mongService.deleteMong();
        return ResponseEntity.ok().body(new SuccessResponse(null));
    }
}
