package com.paymong.mong.controller;

import com.paymong.global.response.SuccessResponse;
import com.paymong.mong.dto.request.ModifyEatFoodReqDto;
import com.paymong.mong.dto.request.ModifyEatSnackReqDto;
import com.paymong.mong.dto.request.ModifyTrainingReqDto;
import com.paymong.mong.dto.request.ModifyWalkingReqDto;
import com.paymong.mong.dto.response.*;
import com.paymong.mong.service.ManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 기존 v1 앱의 매핑 때문에 안드로이드 버전 업데이트 이후 매핑 수정 에정 (이전 버전 서비스 유지를 위함)
@Slf4j
@RestController
@RequiredArgsConstructor
public class ManagementController {
    private final ManagementService managementService;

    @PutMapping("/management/stroke")
    private ResponseEntity<Object> modifyStroke() {
        ModifyStrokeResDto modifyStrokeResDto = managementService.modifyStroke();
        return ResponseEntity.ok().body(new SuccessResponse(modifyStrokeResDto));
    }

    @PutMapping("/management/sleep/toggle")
    private ResponseEntity<Object> modifySleep() {
        ModifySleepResDto modifySleepResDto = managementService.modifySleep();
        return ResponseEntity.ok().body(new SuccessResponse(modifySleepResDto));
    }

    @PutMapping("/management/poop")
    private ResponseEntity<Object> modifyPoop() {
        ModifyPoopResDto modifyPoopResDto = managementService.modifyPoop();
        return ResponseEntity.ok().body(new SuccessResponse(modifyPoopResDto));
    }

    @PutMapping("/management/feed/food")
    private ResponseEntity<Object> modifyEatFood(@RequestBody ModifyEatFoodReqDto modifyEatFoodReqDto) {
        ModifyEatFoodResDto modifyEatFoodResDto = managementService.modifyEatFood(modifyEatFoodReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(modifyEatFoodResDto));
    }

    @PutMapping("/management/feed/snack")
    private ResponseEntity<Object> modifyEatSnack(@RequestBody ModifyEatSnackReqDto modifyEatSnackReqDto) {
        ModifyEatSnackResDto modifyEatSnackResDto = managementService.modifyEatSnack(modifyEatSnackReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(modifyEatSnackResDto));
    }

    @PutMapping("/management/training")
    private ResponseEntity<Object> modifyTraining(@RequestBody ModifyTrainingReqDto modifyTrainingReqDto) {
        ModifyTrainingResDto modifyTrainingResDto = managementService.modifyTraining(modifyTrainingReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(modifyTrainingResDto));
    }

    @PutMapping("/management/training/walking")
    private ResponseEntity<Object> modifyWalking(@RequestBody ModifyWalkingReqDto modifyWalkingReqDto) {
        ModifyWalkingResDto modifyWalkingResDto = managementService.modifyWalking(modifyWalkingReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(modifyWalkingResDto));
    }

    @PutMapping("/management/evolution")
    private ResponseEntity<Object> modifyEvolution() {
        ModifyEvolutionResDto modifyEvolutionResDto = managementService.modifyEvolution();
        return ResponseEntity.ok().body(new SuccessResponse(modifyEvolutionResDto));
    }

    @PutMapping("/management/graduation")
    private ResponseEntity<Object> modifyGraduation() {
        ModifyGraduationResDto modifyGraduationResDto = managementService.modifyGraduation();
        return ResponseEntity.ok().body(new SuccessResponse(modifyGraduationResDto));
    }
}
