package com.paymong.collect.service;

import com.paymong.collect.dto.request.RegisterMapCollectReqDto;
import com.paymong.collect.dto.request.RegisterMongCollectReqDto;
import com.paymong.collect.dto.response.FindMapCollectResDto;
import com.paymong.collect.dto.response.FindMongCollectResDto;
import com.paymong.collect.entity.MapCollect;
import com.paymong.collect.entity.MongCollect;
import com.paymong.global.code.CollectFailCode;
import com.paymong.global.security.CustomUserDetail;
import com.paymong.collect.repository.MapCollectMapping;
import com.paymong.collect.repository.MapCollectRepository;
import com.paymong.collect.repository.MongCollectMapping;
import com.paymong.collect.repository.MongCollectRepository;
import com.paymong.global.exception.fail.InvalidFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectService {
    private final MapCollectRepository mapCollectRepository;
    private final MongCollectRepository mongCollectRepository;

    @Transactional
    public List<FindMapCollectResDto> findMapCollect() throws RuntimeException {
        Long memberId = getMemberId();

        List<MapCollectMapping> mapCollectMappingList = mapCollectRepository.findByMemberId(memberId);

        return FindMapCollectResDto.toList(mapCollectMappingList);
    }

    @Transactional
    public FindMongCollectResDto findMongCollect() throws RuntimeException {
        Long memberId = getMemberId();

        List<MongCollectMapping> mongCollectMappingList = mongCollectRepository.findByMemberId(memberId);

        return FindMongCollectResDto.toList(mongCollectMappingList);
    }

    @Transactional
    public void registerMapCollect(RegisterMapCollectReqDto registerMapCollectReqDto) throws RuntimeException {
        Long memberId = getMemberId();
        String mapCode = registerMapCollectReqDto.getMapCode();

        try {
            Optional<MapCollect> mapCollect = mapCollectRepository.findByMemberIdAndMapCode(memberId, mapCode);

            if (mapCollect.isPresent())
                throw new InvalidFailException(CollectFailCode.DUPLICATION_MAP_CODE);

            mapCollectRepository.save(MapCollect.builder()
                    .memberId(memberId)
                    .mapCode(mapCode)
                    .build());
        } catch (DataIntegrityViolationException e) {
            // common_code 에 등록되지 않은 맵 코드를 입력하여 등록을 시도할 때 예외 처리
            throw new InvalidFailException(CollectFailCode.INVALID_MAP_CODE);
        }
    }

    @Transactional
    public void registerMongCollect(RegisterMongCollectReqDto registerMongCollectReqDto) throws RuntimeException {
        Long memberId = getMemberId();
        String mongCode = registerMongCollectReqDto.getMongCode();

        try {
            Optional<MongCollect> mongCollect = mongCollectRepository.findByMemberIdAndMongCode(memberId, mongCode);
            if (mongCollect.isPresent())
                throw new InvalidFailException(CollectFailCode.DUPLICATION_MONG_CODE);

            mongCollectRepository.save(MongCollect.builder()
                    .memberId(memberId)
                    .mongCode(mongCode)
                    .build());
        } catch (DataIntegrityViolationException e) {
            // common_code 에 등록되지 않은 몽 코드를 입력하여 등록을 시도할 때 예외 처리
            throw new InvalidFailException(CollectFailCode.INVALID_MONG_CODE);
        }
    }

    private Long getMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();

        String memberId = customUserDetail.getUsername();
        return Long.parseLong(memberId);
    }
}
