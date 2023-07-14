package com.paymong.mong.service;

import com.paymong.core.exception.fail.InvalidFailException;
import com.paymong.core.exception.fail.NotFoundFailException;
import com.paymong.mong.dto.MongStatusDto;
import com.paymong.mong.dto.request.*;
import com.paymong.mong.dto.response.*;
import com.paymong.mong.entity.*;
import com.paymong.mong.global.client.PayPointService;
import com.paymong.mong.global.code.MongActiveCode;
import com.paymong.mong.global.code.MongFailCode;
import com.paymong.mong.global.security.CustomUserDetail;
import com.paymong.mong.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagementService {
    private final MongRepository mongRepository;
    private final MongHistoryRepository mongHistoryRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final ProductCodeRepository productCodeRepository;
    private final StatusCodeRepository statusCodeRepository;
    private final PayPointService payPointService;

    @Value("${paymong.active.training.min_count}")
    private Long trainingMinCount;

    @Transactional
    public ModifyStrokeResDto modifyStroke() throws RuntimeException {
        Long mongId = getMongId();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_MONG));

        int nowStrokeCount = mong.getStrokeCount();
        mong.setStrokeCount(nowStrokeCount + 1);

        // 활동 기록 저장
        mongHistoryRepository.save(MongHistory.builder()
                .mongId(mongId)
                .code(MongActiveCode.STROKE.code)
                .build());

        return ModifyStrokeResDto.of(mong, MongStatusDto.of(mong));
    }

    @Transactional
    public ModifySleepResDto modifySleep() throws RuntimeException {
        Long mongId = getMongId();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_MONG));

        boolean isSleep = mong.getIsSleep();
        mong.setIsSleep(!isSleep);

        // 활동 기록 저장
        mongHistoryRepository.save(MongHistory.builder()
                .mongId(mongId)
                .code(MongActiveCode.SLEEP.code)
                .build());

        return ModifySleepResDto.of(mong, MongStatusDto.of(mong));
    }

    @Transactional
    public ModifyPoopResDto modifyPoop() throws RuntimeException {
        Long mongId = getMongId();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_MONG));

        mong.setPoopCount(0);

        // 활동 기록 저장
        mongHistoryRepository.save(MongHistory.builder()
                .mongId(mongId)
                .code(MongActiveCode.POOP.code)
                .build());

        return ModifyPoopResDto.of(mong, MongStatusDto.of(mong));
    }

    @Transactional
    public ModifyEatFoodResDto modifyEatFood(ModifyEatFoodReqDto modifyEatFoodReqDto) throws RuntimeException {
        Long mongId = getMongId();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_MONG));

        String foodCode = modifyEatFoodReqDto.getFoodCode();

        // 음식에 대한 스텟 적용
        ProductCode productCode = productCodeRepository.findByCode(foodCode)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_FOOD));
        CommonCode commonCode = commonCodeRepository.findById(foodCode)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_FOOD));

        // pay point Service 호출 부분
        String content = String.format("음식 %s 구매", commonCode.getName());
        Integer price = productCode.getPrice();
        String code = commonCode.getCode();
        payPointService.registerPayPoint(RegisterPayPointReqDto.builder()
                .content(content)
                .price(-price)
                .code(code)
                .build());

        setMongStatus(mong, productCode.getStatusCode());

        // 활동 기록 저장
        mongHistoryRepository.save(MongHistory.builder()
                .mongId(mongId)
                .code(MongActiveCode.FOOD.code)
                .build());

        return ModifyEatFoodResDto.of(mong, MongStatusDto.of(mong));
    }

    @Transactional
    public ModifyEatSnackResDto modifyEatSnack(ModifyEatSnackReqDto modifyEatSnackReqDto) throws RuntimeException {
        Long mongId = getMongId();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_MONG));

        String snackCode = modifyEatSnackReqDto.getSnackCode();

        // 간식에 대한 스탯 적용
        ProductCode productCode = productCodeRepository.findByCode(snackCode)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_SNACK));
        CommonCode commonCode = commonCodeRepository.findById(snackCode)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_FOOD));

        // pay point Service 호출 부분
        String content = String.format("간식 %s 구매", commonCode.getName());
        Integer price = productCode.getPrice();
        String code = commonCode.getCode();
        payPointService.registerPayPoint(RegisterPayPointReqDto.builder()
                .content(content)
                .price(-price)
                .code(code)
                .build());

        setMongStatus(mong, productCode.getStatusCode());

        // 활동 기록 저장
        mongHistoryRepository.save(MongHistory.builder()
                .mongId(mongId)
                .code(MongActiveCode.SNACK.code)
                .build());

        return ModifyEatSnackResDto.of(mong, MongStatusDto.of(mong));
    }

    @Transactional
    public ModifyTrainingResDto modifyTraining(ModifyTrainingReqDto modifyTrainingReqDto) throws RuntimeException {
        Long mongId = getMongId();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_MONG));

        Integer trainingCount = modifyTrainingReqDto.getTrainingCount();

        // 50회 미만 컷
        if (trainingCount < trainingMinCount)
            throw new InvalidFailException(MongFailCode.INVALID_TRAINING_COUNT);

        StatusCode statusCode = statusCodeRepository.findByCode(MongActiveCode.TRAINING.code)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_ACTIVE));

        // 포인트 갱신 호출
        String content = String.format("훈련 %d회 적립", trainingCount);
        Integer price = statusCode.getPoint();
        String code = statusCode.getCode();
        payPointService.registerPayPoint(RegisterPayPointReqDto.builder()
                .content(content)
                .price(price)
                .code(code)
                .build());

        // 훈련 횟수에 대한 스탯 적용
        setMongStatus(mong, statusCode);

        // 활동 기록 저장
        mongHistoryRepository.save(MongHistory.builder()
                .mongId(mongId)
                .code(MongActiveCode.TRAINING.code)
                .build());

        return ModifyTrainingResDto.of(mong, MongStatusDto.of(mong));
    }

    @Transactional
    public ModifyWalkingResDto modifyWalking(ModifyWalkingReqDto modifyWalkingReqDto) throws RuntimeException {
        Long mongId = getMongId();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_MONG));

        Integer walkingCount = modifyWalkingReqDto.getWalkingCount();

        if (walkingCount < 0)
            throw new InvalidFailException(MongFailCode.INVALID_WALKING_COUNT);

        StatusCode statusCode = statusCodeRepository.findByCode(MongActiveCode.WALKING.code)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_ACTIVE));

        // 포인트 갱신 호출
        String content = String.format("훈련 %d보 적립", walkingCount);
        Integer price = statusCode.getPoint();
        String code = statusCode.getCode();
        payPointService.registerPayPoint(RegisterPayPointReqDto.builder()
                .content(content)
                .price(price)
                .code(code)
                .build());

        // 변경사항 적용
        int count = walkingCount / 500;
        StatusCode nextStatus = StatusCode.builder()
                .code(statusCode.getCode())
                .point(statusCode.getPoint() + walkingCount / 10)
                .strength(statusCode.getStrength() * (statusCode.getStrength()) * count)
                .health(statusCode.getHealth())
                .satiety(statusCode.getSatiety())
                .sleep(statusCode.getSleep())
                .weight(statusCode.getWeight())
                .build();

        // 걸음 수에 대한 스탯 적용
        setMongStatus(mong, nextStatus);

        // 활동 기록 저장
        mongHistoryRepository.save(MongHistory.builder()
                .mongId(mongId)
                .code(MongActiveCode.WALKING.code)
                .build());

        return ModifyWalkingResDto.of(mong, MongStatusDto.of(mong));
    }

    @Transactional
    public ModifyEvolutionResDto modifyEvolution() throws RuntimeException {
        Long mongId = getMongId();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_MONG));

        // 진화 로직 수행

        // 활동 기록 저장
        mongHistoryRepository.save(MongHistory.builder()
                .mongId(mongId)
                .code(MongActiveCode.EVOLUTION.code)
                .build());

        return ModifyEvolutionResDto.builder()
                .mongCode(mong.getMongCode())
                .stateCode(mong.getStatusCode())
                .build();
    }

    @Transactional
    public ModifyGraduationResDto modifyGraduation() throws RuntimeException {
        Long mongId = getMongId();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(MongFailCode.NOT_FOUND_MONG));

        // 졸업 로직 수행

        // 활동 기록 저장
        mongHistoryRepository.save(MongHistory.builder()
                .mongId(mongId)
                .code(MongActiveCode.GRADUATION.code)
                .build());

        return ModifyGraduationResDto.builder()
                .mongCode(mong.getMongCode())
                .build();
    }

    private void setMongStatus(Mong mong, StatusCode statusCode) {
        Integer strength = mong.getStrength();
        Integer health = mong.getHealth();
        Integer satiety = mong.getSatiety();
        Integer sleep = mong.getSleep();
        Integer weight = mong.getWeight();

        mong.setStrength(strength + statusCode.getStrength());
        mong.setHealth(health + statusCode.getHealth());
        mong.setSatiety(satiety + statusCode.getSatiety());
        mong.setSleep(sleep + statusCode.getSleep());
        mong.setWeight(weight + statusCode.getWeight());
    }

    private Long getMongId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();

        String mongId = customUserDetail.getMongId();
        return Long.parseLong(mongId);
    }
}
