package com.paymong.global.scheduler.interval;

import com.paymong.global.code.*;
import com.paymong.global.exception.fail.NotFoundFailException;
import com.paymong.global.scheduler.BasicScheduler;
import com.paymong.global.task.interval.DeepSleepTask;
import com.paymong.global.vo.SchedulerWithIntervalVo;
import com.paymong.lifecycle.entity.Mong;
import com.paymong.lifecycle.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeepSleepScheduler implements BasicSchedulerInterval {
    private static final Map<Long, SchedulerWithIntervalVo> deepSleepSchedulerMap =  new HashMap<>();
    private static final Set<Long> deepSleepSet = new HashSet<>();
    private final DeepSleepTask deepSleepTask;

    @Value("${scheduler.logger}")
    private Boolean logger;

    private final MongRepository mongRepository;

    @Override
    public void startScheduler(Long mongId) {
        // 서버 부팅 때 또는 새로운 캐릭터 생성 시 한번 실행되는 스케줄러
        if (deepSleepSchedulerMap.containsKey(mongId)) {
            BasicScheduler.writeLog(mongId, SchedulerMessage.DEEP_SLEEP_ALREADY_LOCK);
            return;
        }

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(LifeCycleFailCode.NOT_FOUND_MONG));

//        LocalTime sleepStart = mong.getSleepStart();
//        LocalTime sleepEnd = mong.getSleepEnd();

        // TEST - ASC
//        LocalTime sleepStart = LocalTime.of(15, 0, 5);
//        LocalTime sleepEnd = LocalTime.of(15, 0, 15);
        // TEST - DESC
        LocalDateTime now = LocalDateTime.of(2023, 1, 1, 0, 0, 0);

        LocalTime nowTime = now.toLocalTime();
        LocalTime sleepStart = LocalTime.of(23, 59, 55);
        LocalTime sleepEnd = LocalTime.of(0, 0, 5);

        LocalDateTime start;
        LocalDateTime end;

        if (nowTime.isBefore(sleepStart))
            start = LocalDateTime.of(now.toLocalDate().minusDays(1), sleepStart);
        else
            start = LocalDateTime.of(now.toLocalDate().plusDays(1), sleepStart);

        if (nowTime.isBefore(sleepEnd))
            end = LocalDateTime.of(now.toLocalDate(), sleepEnd);
        else
            end = LocalDateTime.of(now.toLocalDate().plusDays(1), sleepEnd);


//        LocalDateTime start = LocalDateTime.of(now.toLocalDate()
//                .plusDays(now.toLocalTime().isAfter(sleepStart) ? -1 : 0), sleepStart);
//        LocalDateTime end = LocalDateTime.of(now.toLocalDate()
//                .plusDays(now.toLocalTime().isAfter(sleepStart) ? 1 : 0)
//                .plusDays(sleepStart.isAfter(sleepEnd) ? 1 : 0), sleepEnd);

        log.info("now : {}, start : {}, end : {}", now, start, end);

        SchedulerWithIntervalVo schedulerWithIntervalVo;
        // 정기 수면 돌입
        if (now.isAfter(start) || now.isEqual(start)) {
            long interval = Duration.between(now, end).getSeconds();
            schedulerWithIntervalVo = deepSleepTask.goSleep(this, mongId, interval);
            registerSet(mongId);
        }
        // 수면 대기 돌입
        else {
            long interval = Duration.between(now, start).getSeconds();
            schedulerWithIntervalVo = deepSleepTask.wakeUp(this, mongId, interval);
            deleteSet(mongId);
        }

        // 새로운 스케줄러 실행 및 저장
        registerScheduler(mongId, schedulerWithIntervalVo);

        BasicScheduler.writeLogWithInterval(mongId, SchedulerMessage.DEEP_SLEEP_START, schedulerWithIntervalVo.getInterval());
    }

    @Override
    public void stopScheduler(Long mongId) {
        if (deepSleepSchedulerMap.containsKey(mongId)) {
            // Deep Sleep 스케줄러 중지
            SchedulerWithIntervalVo schedulerWithIntervalVo = deepSleepSchedulerMap.get(mongId);
            schedulerWithIntervalVo.getScheduler().shutdown();
            // Deep Sleep 스케줄러 Map 에서 삭제
            deepSleepSchedulerMap.remove(mongId);

            if (logger)
                BasicScheduler.writeLog(mongId, SchedulerMessage.DEEP_SLEEP_STOP);
        } else
            BasicScheduler.writeLog(mongId, SchedulerMessage.DEEP_SLEEP_NOT_FOUND);
    }

    @Override
    public Runnable getRunnable(Long mongId) {
        return () -> {
            deepSleepTask.afterStop(mongId, this);
        };
    }

    public void registerScheduler(Long mongId, SchedulerWithIntervalVo schedulerWithIntervalVo) {
        schedulerWithIntervalVo.init();
        deepSleepSchedulerMap.put(mongId, schedulerWithIntervalVo);
    }

    public void registerSet(Long mongId) {
        deepSleepSet.add(mongId);
    }

    public void deleteSet(Long mongId) {
        deepSleepSet.remove(mongId);
    }

    public Boolean isDeepSleep(Long mongId) {
        return deepSleepSet.contains(mongId);
    }
}
