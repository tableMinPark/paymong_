package com.paymong.paypoint.controller;

import com.paymong.core.exception.fail.InvalidFailException;
import com.paymong.core.response.SuccessResponse;
import com.paymong.paypoint.dto.request.RegisterPayPointReqDto;
import com.paymong.paypoint.dto.response.FindFoodAndSnack;
import com.paymong.paypoint.dto.response.FindPayPointInfoResDto;
import com.paymong.paypoint.dto.response.FindPayPointResDto;
import com.paymong.paypoint.global.code.PayPointFailCode;
import com.paymong.paypoint.service.PayPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
// 기존 v1 앱의 매핑 때문에 안드로이드 버전 업데이트 이후 매핑 수정 에정 (이전 버전 서비스 유지를 위함)
//@RequestMapping("/member")
public class PayPointController {
    private final PayPointService payPointService;

    @GetMapping("/member/info")
    private ResponseEntity<Object> findPayPoint() {
        FindPayPointResDto findPayPointResDto = payPointService.findPayPoint();
        return ResponseEntity.ok().body(new SuccessResponse(findPayPointResDto));
    }

    @GetMapping("/member/paypoint/list")
    private ResponseEntity<Object> findPayPointInfo() {
        List<FindPayPointInfoResDto> findPayPointInfoResDtoList = payPointService.findPayPointInfo();
        return ResponseEntity.ok().body(new SuccessResponse(findPayPointInfoResDtoList));
    }

    @GetMapping("/common/food/{foodCategory}")
    private ResponseEntity<Object> findFoodAndSnack(@PathVariable("foodCategory") String foodCategory) {
        List<FindFoodAndSnack> findFoodAndSnackList = payPointService.findFoodAndSnack(foodCategory);
        return ResponseEntity.ok().body(new SuccessResponse(findFoodAndSnackList));
    }

    @PostMapping("/member/paypoint")
    private ResponseEntity<Object> registerPayPoint(@RequestBody RegisterPayPointReqDto registerPayPointReqDto) {
        if (registerPayPointReqDto.getContent() == null || registerPayPointReqDto.getPrice() == null)
            throw new InvalidFailException(PayPointFailCode.INVALID_REQUEST_DTO);
        if (!StringUtils.hasText(registerPayPointReqDto.getContent()))
            throw new InvalidFailException(PayPointFailCode.INVALID_CONTENT);

        payPointService.registerPayPoint(registerPayPointReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(true));
    }
}
