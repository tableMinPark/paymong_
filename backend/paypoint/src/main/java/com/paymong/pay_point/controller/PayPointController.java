package com.paymong.pay_point.controller;

import com.paymong.core.exception.fail.InvalidFailException;
import com.paymong.core.response.SuccessResponse;
import com.paymong.pay_point.dto.request.RegisterPayPointReqDto;
import com.paymong.pay_point.dto.response.FindPayPointInfoResDto;
import com.paymong.pay_point.dto.response.FindPayPointResDto;
import com.paymong.pay_point.global.code.PayPointFailCode;
import com.paymong.pay_point.service.PayPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class PayPointController {
    private final PayPointService payPointService;

    @GetMapping("/info")
    private ResponseEntity<Object> findPayPoint() {
        FindPayPointResDto findPayPointResDto = payPointService.findPayPoint();
        return ResponseEntity.ok().body(new SuccessResponse(findPayPointResDto));
    }

    @GetMapping("/paypoint/list")
    private ResponseEntity<Object> findPayPointInfo() {
        List<FindPayPointInfoResDto> findPayPointInfoResDtoList = payPointService.findPayPointInfo();
        return ResponseEntity.ok().body(new SuccessResponse(findPayPointInfoResDtoList));
    }

    @PostMapping("/paypoint")
    private ResponseEntity<Object> registerPayPoint(@RequestBody RegisterPayPointReqDto registerPayPointReqDto) {
        if (registerPayPointReqDto.getContent() == null || registerPayPointReqDto.getPrice() == null)
            throw new InvalidFailException(PayPointFailCode.INVALID_REQUEST_DTO);
        if (!StringUtils.hasText(registerPayPointReqDto.getContent()))
            throw new InvalidFailException(PayPointFailCode.INVALID_CONTENT);

        payPointService.registerPayPoint(registerPayPointReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(true));
    }
}
