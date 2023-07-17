package com.paymong.mong.service;

import com.paymong.client.dto.request.RegisterMongSchedulerReqDto;
import com.paymong.client.dto.request.StopSchedulerReqDto;
import com.paymong.client.service.LifeCycleService;
import com.paymong.global.code.LifeCycleCode;
import com.paymong.global.code.MongConditionCode;
import com.paymong.global.exception.fail.ClientFailException;
import com.paymong.global.exception.fail.InvalidFailException;
import com.paymong.global.exception.fail.NotFoundFailException;
import com.paymong.mong.dto.request.RegisterMongReqDto;
import com.paymong.mong.dto.response.FindMongInfoResDto;
import com.paymong.mong.dto.response.FindMongResDto;
import com.paymong.mong.dto.response.FindMongStatusResDto;
import com.paymong.mong.entity.CommonCode;
import com.paymong.mong.entity.MemberMap;
import com.paymong.mong.entity.Mong;
import com.paymong.global.code.MongFailCode;
import com.paymong.global.security.CustomUserDetail;
import com.paymong.mong.repository.CommonCodeRepository;
import com.paymong.mong.repository.MemberMapRepository;
import com.paymong.mong.repository.MongRepository;
import com.paymong.mong.dto.common.MongStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MongService {
    private final MongRepository mongRepository;
    private final MemberMapRepository memberMapRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final LifeCycleService lifeCycleService;

    @Transactional
    public FindMongResDto findMong() throws RuntimeException {
        Long memberId = getMemberId();
        Long mongId = getMongId();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_MONG));

        MemberMap memberMap = memberMapRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_SNACK));

        return FindMongResDto.builder()
                .mongId(mong.getMongId())
                .name(mong.getName())
                .mapCode(memberMap.getCode().getCode())
                .mongCode(mong.getMongCode())
                .stateCode(mong.getStatusCode())
                .poopCount(mong.getPoopCount())
                .build();
    }

    @Transactional
    public FindMongInfoResDto findMongInfo() throws RuntimeException {
        Long mongId = getMongId();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_MONG));

        return FindMongInfoResDto.builder()
                .name(mong.getName())
                .weight(mong.getWeight())
                .born(mong.getRegDt())
                .build();
    }

    @Transactional
    public FindMongStatusResDto findMongStatus() throws RuntimeException {
        Long mongId = getMongId();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_MONG));


        return FindMongStatusResDto.of(
                mong.getMongId(),
                mong.getName(),
                MongStatusDto.of(mong));
    }

    @Transactional
    public void registerMong(RegisterMongReqDto registerMongReqDto) throws RuntimeException {
        Long memberId = getMemberId();
        Long mongId = getMongId();

        // 생성된 몽이 있는 경우
        if (!mongId.equals(-1L)) {
            // 기존 몽 죽음 여부 확인해서 비활성화
            mongRepository.findById(mongId)
                    .ifPresent(m -> {
                        // 죽었으면 비활성화 처리
                        if (m.getStatusCode().equals(MongConditionCode.DIE.code)) {
                            // 비활성화 처리
                            m.setActive(false);
                            // 기존 몽과 관련된 스케줄러 중지
                            lifeCycleService.stopScheduler(StopSchedulerReqDto.builder()
                                    .code(List.of(
                                            LifeCycleCode.POOP,
                                            LifeCycleCode.HEALTH,
                                            LifeCycleCode.SATIETY,
                                            LifeCycleCode.DEATH,
                                            LifeCycleCode.EVOLUTION,
                                            LifeCycleCode.SLEEP))
                                    .build());
                        }
                        // 죽지 않았으면 몽이 존재하기 떄문에 생성 불가 처리
                        else
                            throw new InvalidFailException(MongFailCode.REGISTER_MONG);
                    });
        }

        List<CommonCode> mongCodeList = commonCodeRepository.findByCodeForMongCode(0);

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int index = random.nextInt(mongCodeList.size());

        String mongCode = mongCodeList.get(index).getCode();

        // 몽 저장
        Mong mong = Mong.of(memberId,
                mongCode,
                registerMongReqDto.getName(),
                registerMongReqDto.getSleepStart(),
                registerMongReqDto.getSleepEnd());

        mongRepository.save(mong);

        // 스케줄러 시작 목록
        RegisterMongSchedulerReqDto registerMongSchedulerReqDto = RegisterMongSchedulerReqDto.builder()
                .mongId(mong.getMongId())
                .code(List.of(
                        LifeCycleCode.HEALTH,
                        LifeCycleCode.POOP,
                        LifeCycleCode.SATIETY,
                        LifeCycleCode.SLEEP))
                .build();

        // 스케줄러 시작 요청
        if (!lifeCycleService.registerMongScheduler(registerMongSchedulerReqDto))
            throw new ClientFailException(MongFailCode.CLIENT_START_SCHEDULER);
    }

    @Transactional
    public void deleteMong() throws RuntimeException {
        Long mongId = getMongId();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_MONG));

        mong.setActive(false);
    }

    private Long getMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();

        String memberId = customUserDetail.getMemberId();
        return Long.parseLong(memberId);
    }

    private Long getMongId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();

        String mongId = customUserDetail.getMongId();
        return Long.parseLong(mongId);
    }
}
