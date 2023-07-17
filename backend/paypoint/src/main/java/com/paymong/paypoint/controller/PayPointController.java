package com.paymong.paypoint.controller;

import com.paymong.global.exception.fail.InvalidFailException;
import com.paymong.global.response.SuccessResponse;
import com.paymong.paypoint.dto.request.RegisterPayPointReqDto;
import com.paymong.paypoint.dto.request.RegisterPointReqDto;
import com.paymong.paypoint.dto.response.FindFoodAndSnackResDto;
import com.paymong.paypoint.dto.response.FindMemberMapResDto;
import com.paymong.paypoint.dto.response.FindPayPointInfoResDto;
import com.paymong.paypoint.dto.response.FindPayPointResDto;
import com.paymong.global.code.PayPointFailCode;
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
        List<FindFoodAndSnackResDto> findFoodAndSnackResDtoList = payPointService.findFoodAndSnack(foodCategory);
        return ResponseEntity.ok().body(new SuccessResponse(findFoodAndSnackResDtoList));
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

    @PutMapping("/member/paypoint")
    private ResponseEntity<Object> registerPoint(@RequestBody RegisterPointReqDto registerPointReqDto) {
        if (registerPointReqDto.getContent() == null || registerPointReqDto.getPrice() == null)
            throw new InvalidFailException(PayPointFailCode.INVALID_REQUEST_DTO);
        if (!StringUtils.hasText(registerPointReqDto.getContent()))
            throw new InvalidFailException(PayPointFailCode.INVALID_CONTENT);

        payPointService.registerPoint(registerPointReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(true));
    }

    @GetMapping("/member/map")
    private ResponseEntity<Object> findMemberMap() {
        FindMemberMapResDto findMemberMapResDto = payPointService.findMemberMap();
        return ResponseEntity.ok().body(new SuccessResponse(findMemberMapResDto));
    }
}
