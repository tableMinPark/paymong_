package com.paymong.global.scheduler.expire;

import com.paymong.global.code.SchedulerExpire;
import com.paymong.global.code.SchedulerMessage;
import com.paymong.global.scheduler.BasicScheduler;
import com.paymong.global.task.expire.SleepTask;
import com.paymong.global.vo.SchedulerWithExpireVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SleepScheduler implements BasicSchedulerExpire {
    private static final Map<Long, SchedulerWithExpireVo> sleepSchedulerMap =  new HashMap<>();
    private final SleepTask sleepTask;

    @Value("${scheduler.logger}")
    private Boolean logger;

    @Override
    public void startScheduler(Long mongId) {


        // 새로운 Sleep 스케줄러 생성
        SchedulerWithExpireVo schedulerWithExpireVo = SchedulerWithExpireVo.builder()
                .startTime(LocalDateTime.now())
                .expire(SchedulerExpire.SLEEP_EXPIRE.expire)
                .message(SchedulerExpire.SLEEP_EXPIRE.message)
                .runnable(getRunnable(mongId))
                .build();
        // 초기화
        schedulerWithExpireVo.init();
        // Sleep 스케줄러 Map 에 저장
        sleepSchedulerMap.put(mongId, schedulerWithExpireVo);

        BasicScheduler.writeLogWithExpire(mongId, SchedulerMessage.SLEEP_START, schedulerWithExpireVo.getExpire());
    }

    @Override
    public void pauseScheduler(Long mongId) {
        if (sleepSchedulerMap.containsKey(mongId)) {
            // Sleep 스케줄러 일시정지
            SchedulerWithExpireVo schedulerWithExpireVo = sleepSchedulerMap.get(mongId);
            Long pastTime = Duration.between(schedulerWithExpireVo.getStartTime(), LocalDateTime.now()).toSeconds();
            Long expire = schedulerWithExpireVo.getExpire() - pastTime;

            schedulerWithExpireVo.getScheduler().shutdown();
            schedulerWithExpireVo.setExpire(expire);

            BasicScheduler.writeLogWithExpire(mongId, SchedulerMessage.SLEEP_PAUSE, expire);
        } else
            BasicScheduler.writeLog(mongId, SchedulerMessage.SLEEP_NOT_FOUND);
    }

    @Override
    public void restartScheduler(Long mongId) {
        if (sleepSchedulerMap.containsKey(mongId)) {
            // Sleep 스케줄러 재시작 (일시중지 스케줄러 재가동)
            SchedulerWithExpireVo schedulerWithExpireVo = sleepSchedulerMap.get(mongId);
            schedulerWithExpireVo.setStartTime(LocalDateTime.now());
            schedulerWithExpireVo.init();

            BasicScheduler.writeLogWithExpire(mongId, SchedulerMessage.SLEEP_RESTART, schedulerWithExpireVo.getExpire());
        } else
            BasicScheduler.writeLog(mongId, SchedulerMessage.SLEEP_NOT_FOUND);
    }

    @Override
    public void stopScheduler(Long mongId) {
        if (sleepSchedulerMap.containsKey(mongId)) {
            // Sleep 스케줄러 중지
            SchedulerWithExpireVo schedulerWithExpireVo = sleepSchedulerMap.get(mongId);
            schedulerWithExpireVo.getScheduler().shutdown();
            // Sleep 스케줄러 Map 에서 삭제
            sleepSchedulerMap.remove(mongId);

            if (logger)
                BasicScheduler.writeLog(mongId, SchedulerMessage.SLEEP_STOP);
        } else
            BasicScheduler.writeLog(mongId, SchedulerMessage.SLEEP_NOT_FOUND);
    }

    @Override
    public Runnable getRunnable(Long mongId) {
        return () -> {
            sleepTask.afterStop(mongId, this);
        };
    }

    public Boolean hasSleepSchedule(Long mongId) {
        return sleepSchedulerMap.containsKey(mongId);
    }
}
