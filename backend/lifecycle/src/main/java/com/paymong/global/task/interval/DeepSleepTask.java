package com.paymong.global.task.interval;

import com.paymong.global.code.*;
import com.paymong.global.exception.fail.NotFoundFailException;
import com.paymong.global.scheduler.*;
import com.paymong.global.scheduler.expire.DeathScheduler;
import com.paymong.global.scheduler.expire.EvolutionScheduler;
import com.paymong.global.scheduler.expire.SleepScheduler;
import com.paymong.global.scheduler.interval.DeepSleepScheduler;
import com.paymong.global.scheduler.interval.HealthScheduler;
import com.paymong.global.scheduler.interval.PoopScheduler;
import com.paymong.global.scheduler.interval.SatietyScheduler;
import com.paymong.global.task.BasicTask;
import com.paymong.global.vo.SchedulerWithIntervalVo;
import com.paymong.lifecycle.entity.Mong;
import com.paymong.lifecycle.repository.MongHistoryRepository;
import com.paymong.lifecycle.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeepSleepTask implements BasicTask {
    private static final Long DAY_SECONDS = 86400L;

    private final MongRepository mongRepository;
    private final MongHistoryRepository mongHistoryRepository;

    private final DeathScheduler deathScheduler;
    private final EvolutionScheduler evolutionScheduler;
    private final HealthScheduler healthScheduler;
    private final PoopScheduler poopScheduler;
    private final SatietyScheduler satietyScheduler;
    private final SleepScheduler sleepScheduler;

    @Override
    @Transactional
    public void afterStop(Long mongId, BasicScheduler basicScheduler) throws NotFoundFailException {
//        Mong mong = mongRepository.findById(mongId)
//                .orElseThrow(() -> new NotFoundFailException(LifeCycleFailCode.NOT_FOUND_MONG));

//        // 꺠움 후 대기 스케줄러
//        if (mong.getIsSleep()) {
//            mong.setIsSleep(false);
//            mong.setStatusCode(BasicScheduler.getStatusCode(mong));
//            // 진화 스케줄러 재시작
//            evolutionScheduler.restartScheduler(mongId);
//            // 인터발 스케줄러 시작
//            healthScheduler.startScheduler(mongId);
//            poopScheduler.startScheduler(mongId);
//            satietyScheduler.stopScheduler(mongId);
//        }
//        // 정기 수면 돌입
//        else {
//            mong.setIsSleep(true);
//            mong.setStatusCode(MongConditionCode.SLEEP.code);
//            // 진화 스케줄러 일시중지
//            evolutionScheduler.pauseScheduler(mongId);
//            // 인터발 스케줄러 중지
//            healthScheduler.stopScheduler(mongId);
//            poopScheduler.stopScheduler(mongId);
//            satietyScheduler.stopScheduler(mongId);
//            // 일반 수면 중지
//            sleepScheduler.stopScheduler(mongId);
//        }

//        LocalTime sleepStart = LocalTime.of(15, 0, 5);
//        LocalTime sleepEnd = LocalTime.of(22, 0, 0);
//
//        LocalDateTime now = LocalDateTime.of(2023, 1, 1, 15, 0, 5);
//        LocalDateTime start = LocalDateTime.of(now.toLocalDate(), sleepStart);
//        LocalDateTime end = LocalDateTime.of(now.toLocalDate().plusDays(sleepStart.isAfter(sleepEnd) ? 1 : 0), sleepEnd);

        // 기존 스케줄러 중지
        basicScheduler.stopScheduler(mongId);

//        LocalTime sleepStart = mong.getSleepStart();
//        LocalTime sleepEnd = mong.getSleepEnd();
        // TEST - ASC
//        LocalTime sleepStart = LocalTime.of(15, 0, 5);
//        LocalTime sleepEnd = LocalTime.of(15, 0, 15);
        // TEST - DESC
        LocalTime sleepStart = LocalTime.of(0, 0, 0);
        LocalTime sleepEnd = LocalTime.of(0, 0, 0);

        DeepSleepScheduler deepSleepScheduler = (DeepSleepScheduler) basicScheduler;

        Boolean isDeepSleep = deepSleepScheduler.isDeepSleep(mongId);

        // 수면 시간 계산 (초)
        long seconds = Duration.between(sleepStart, sleepEnd).getSeconds();
        long interval = seconds <= 0 ? DAY_SECONDS - Math.abs(seconds) : seconds;
        SchedulerWithIntervalVo schedulerWithIntervalVo;
        log.info("interval : {}, isDeepSleep : {}", interval, isDeepSleep);
        // 정기 수면 돌입
        if (!isDeepSleep || interval == DAY_SECONDS) {
            schedulerWithIntervalVo = goSleep(deepSleepScheduler, mongId, interval);
            deepSleepScheduler.registerSet(mongId);
        }
        // 수면 대기 돌입
        else {
//            interval = DAY_SECONDS - interval;
            interval = 15L;
            schedulerWithIntervalVo = wakeUp(deepSleepScheduler, mongId, interval);
            deepSleepScheduler.deleteSet(mongId);
        }

        // 새로운 스케줄러 실행
        deepSleepScheduler.registerScheduler(mongId, schedulerWithIntervalVo);
    }

    public SchedulerWithIntervalVo wakeUp(BasicScheduler basicScheduler, Long mongId, Long interval) throws NotFoundFailException {
        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(LifeCycleFailCode.NOT_FOUND_MONG));

//        mong.setIsSleep(false);
//        mong.setStatusCode(BasicScheduler.getStatusCode(mong));
        BasicScheduler.writeLogWithExpire(mongId, SchedulerMessage.DEEP_SLEEP_NOT_TIME_TO_SLEEP, interval);

        return SchedulerWithIntervalVo.builder()
                .message(SchedulerInterval.DEEP_SLEEP_INTERVAL.message)
                .interval(interval)
                .runnable(basicScheduler.getRunnable(mongId))
                .build();
    }

    public SchedulerWithIntervalVo goSleep(BasicScheduler basicScheduler, Long mongId, Long interval) throws NotFoundFailException {
        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(LifeCycleFailCode.NOT_FOUND_MONG));

        //        mong.setIsSleep(true);
//        mong.setStatusCode(MongConditionCode.SLEEP.code);
        BasicScheduler.writeLogWithExpire(mongId, SchedulerMessage.DEEP_SLEEP_TIME_TO_SLEEP, interval);

        return SchedulerWithIntervalVo.builder()
                .message(SchedulerInterval.DEEP_SLEEP_INTERVAL.message)
                .interval(interval)
                .runnable(basicScheduler.getRunnable(mongId))
                .build();
    }
}
