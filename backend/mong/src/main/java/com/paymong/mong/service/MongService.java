package com.paymong.mong.service;

import com.paymong.core.exception.fail.InvalidFailException;
import com.paymong.core.exception.fail.NotFoundFailException;
import com.paymong.mong.dto.request.RegisterMongReqDto;
import com.paymong.mong.dto.response.FindMongInfoResDto;
import com.paymong.mong.dto.response.FindMongResDto;
import com.paymong.mong.dto.response.FindMongStatusResDto;
import com.paymong.mong.entity.CommonCode;
import com.paymong.mong.entity.Mong;
import com.paymong.mong.global.code.MongFailCode;
import com.paymong.mong.global.security.CustomUserDetail;
import com.paymong.mong.repository.CommonCodeRepository;
import com.paymong.mong.repository.MongRepository;
import com.paymong.mong.dto.MongStatusDto;
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
    private final CommonCodeRepository commonCodeRepository;

    @Transactional
    public FindMongResDto findMong() throws RuntimeException {
        Long mongId = getMongId();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_MONG));

        return FindMongResDto.builder()
                .mongId(mong.getMongId())
                .name(mong.getName())
                .mapCode(mong.getMapCode())
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

        // 생성된 몽이 있는 경우 예외 발생
        if (!mongId.equals(-1L))
            throw new InvalidFailException(MongFailCode.REGISTER_MONG);

        List<CommonCode> mongCodeList = commonCodeRepository.findByCodeForMongCode(0);

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int index = random.nextInt(mongCodeList.size());

        String mongCode = mongCodeList.get(index).getCode();

        mongRepository.save(
                Mong.of(memberId,
                        mongCode,
                        registerMongReqDto.getName(),
                        registerMongReqDto.getSleepStart(),
                        registerMongReqDto.getSleepEnd()));
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
