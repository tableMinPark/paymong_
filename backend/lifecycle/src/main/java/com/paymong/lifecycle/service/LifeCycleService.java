package com.paymong.lifecycle.service;

import com.paymong.global.code.LifeCycleCode;
import com.paymong.global.code.LifeCycleFailCode;
import com.paymong.global.code.MongConditionCode;
import com.paymong.global.exception.fail.InvalidFailException;
import com.paymong.global.exception.fail.NotFoundFailException;
import com.paymong.global.scheduler.*;
import com.paymong.global.scheduler.expire.*;
import com.paymong.global.scheduler.interval.DeepSleepScheduler;
import com.paymong.global.scheduler.interval.HealthScheduler;
import com.paymong.global.scheduler.interval.PoopScheduler;
import com.paymong.global.scheduler.interval.SatietyScheduler;
import com.paymong.global.security.CustomUserDetail;
import com.paymong.lifecycle.dto.request.*;
import com.paymong.lifecycle.entity.Mong;
import com.paymong.lifecycle.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class LifeCycleService {
    private final MongRepository mongRepository;

    private final DeathScheduler deathScheduler;
    private final EvolutionScheduler evolutionScheduler;
    private final HealthScheduler healthScheduler;
    private final PoopScheduler poopScheduler;
    private final SatietyScheduler satietyScheduler;
    private final SleepScheduler sleepScheduler;
    private final DeepSleepScheduler deepSleepScheduler;
    private final StrokeScheduler strokeScheduler;

    public void startScheduler(StartSchedulerReqDto startSchedulerReqDto) throws RuntimeException {
        Long mongId = getMongId();

        if (mongId == -1)
            throw new NotFoundFailException(LifeCycleFailCode.NOT_FOUND_MONG);

        List<LifeCycleCode> codeList = startSchedulerReqDto.getCode();

        List<BasicScheduler> schedulerList = codeList.stream()
                .map(this::getScheduler)
                .collect(Collectors.toList());

        schedulerList.forEach(scheduler -> scheduler.startScheduler(mongId));
    }


    public void pauseScheduler(PauseSchedulerReqDto pauseSchedulerReqDto) throws RuntimeException {
        Long mongId = getMongId();

        if (mongId == -1)
            throw new NotFoundFailException(LifeCycleFailCode.NOT_FOUND_MONG);

        List<LifeCycleCode> codeList = pauseSchedulerReqDto.getCode();

        List<BasicScheduler> schedulerList = codeList.stream()
                .map(this::getScheduler)
                .filter(scheduler -> scheduler instanceof BasicSchedulerExpire)
                .collect(Collectors.toList());

        schedulerList.forEach(scheduler -> {
            BasicSchedulerExpire basicSchedulerExpire = (BasicSchedulerExpire) scheduler;
            basicSchedulerExpire.pauseScheduler(mongId);
        });
    }

    public void restartScheduler(RestartSchedulerReqDto restartSchedulerReqDto) throws RuntimeException {
        Long mongId = getMongId();

        if (mongId == -1)
            throw new NotFoundFailException(LifeCycleFailCode.NOT_FOUND_MONG);

        List<LifeCycleCode> codeList = restartSchedulerReqDto.getCode();

        List<BasicScheduler> schedulerList = codeList.stream()
                .map(this::getScheduler)
                .filter(scheduler -> scheduler instanceof BasicSchedulerExpire)
                .collect(Collectors.toList());

        schedulerList.forEach(scheduler -> {
            BasicSchedulerExpire basicSchedulerExpire = (BasicSchedulerExpire) scheduler;
            basicSchedulerExpire.restartScheduler(mongId);
        });
    }

    public void stopScheduler(StopSchedulerReqDto stopSchedulerReqDto) throws RuntimeException {
        Long mongId = getMongId();

        if (mongId == -1)
            throw new NotFoundFailException(LifeCycleFailCode.NOT_FOUND_MONG);

        List<LifeCycleCode> codeList = stopSchedulerReqDto.getCode();

        List<BasicScheduler> schedulerList = codeList.stream()
                .map(this::getScheduler)
                .collect(Collectors.toList());

        schedulerList.forEach(scheduler -> scheduler.stopScheduler(mongId));
    }

    public void registerMongScheduler(RegisterMongSchedulerReqDto registerMongSchedulerReqDto) {
        Long mongId = registerMongSchedulerReqDto.getMongId();
        List<LifeCycleCode> codeList = registerMongSchedulerReqDto.getCode();

        List<BasicScheduler> schedulerList = codeList.stream()
                .map(this::getScheduler)
                .collect(Collectors.toList());

        schedulerList.forEach(scheduler -> scheduler.startScheduler(mongId));
    }

    public void nextLevelScheduler(NextLevelSchedulerReqDto nextLevelSchedulerReqDto) throws RuntimeException {
        Long mongId = getMongId();

        if (mongId == -1)
            throw new NotFoundFailException(LifeCycleFailCode.NOT_FOUND_MONG);

        Integer level = nextLevelSchedulerReqDto.getLevel();

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(LifeCycleFailCode.NOT_FOUND_MONG));

        // 이미 진화 대기 상태인지 확인
        if (mong.getStatusCode().equals(MongConditionCode.EVOLUTION_READY.code))
            throw new InvalidFailException(LifeCycleFailCode.INVALID_STATUS_CODE);

        evolutionScheduler.nextLevelScheduler(mongId, level);
    }

    private BasicScheduler getScheduler(LifeCycleCode code) throws InvalidFailException {
        if (LifeCycleCode.DEATH.equals(code))
            return deathScheduler;
        else if (LifeCycleCode.EVOLUTION.equals(code))
            return evolutionScheduler;
        else if (LifeCycleCode.HEALTH.equals(code))
            return healthScheduler;
        else if (LifeCycleCode.POOP.equals(code))
            return poopScheduler;
        else if (LifeCycleCode.SATIETY.equals(code))
            return satietyScheduler;
        else if (LifeCycleCode.SLEEP.equals(code))
            return sleepScheduler;
        else if (LifeCycleCode.DEEP_SLEEP.equals(code))
            return deepSleepScheduler;
        else if (LifeCycleCode.STROKE.equals(code))
            return strokeScheduler;
        else
            throw new InvalidFailException(LifeCycleFailCode.INVALID_LIFECYCLE_CODE);
    }

    private Long getMongId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();

        String mongId = customUserDetail.getMongId();

        return Long.parseLong(mongId);
    }
}
