package com.paymong.collect.controller;

import com.paymong.collect.dto.request.RegisterMapCollectReqDto;
import com.paymong.collect.dto.request.RegisterMongCollectReqDto;
import com.paymong.collect.dto.response.FindMapCollectResDto;
import com.paymong.collect.dto.response.FindMongCollectResDto;
import com.paymong.collect.service.CollectService;
import com.paymong.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/collect")
public class CollectController {
    private final CollectService collectService;

    @GetMapping("/map")
    private ResponseEntity<Object> findMapCollect() {
        List<FindMapCollectResDto> findMapCollectResDto = collectService.findMapCollect();
        return ResponseEntity.ok().body(new SuccessResponse(findMapCollectResDto));
    }

    @GetMapping("/mong")
    private ResponseEntity<Object> findMong() {
        FindMongCollectResDto findMongCollectResDto = collectService.findMongCollect();
        return ResponseEntity.ok().body(new SuccessResponse(findMongCollectResDto));
    }

    @PostMapping("/map")
    private ResponseEntity<Object> registerMapCollect(@RequestBody RegisterMapCollectReqDto registerMapCollectReqDto) {
        collectService.registerMapCollect(registerMapCollectReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(null));
    }

    @PostMapping("/mong")
    private ResponseEntity<Object> registerMongCollect(@RequestBody RegisterMongCollectReqDto registerMongCollectReqDto) {
        collectService.registerMongCollect(registerMongCollectReqDto);
        return ResponseEntity.ok().body(new SuccessResponse(null));
    }
}
