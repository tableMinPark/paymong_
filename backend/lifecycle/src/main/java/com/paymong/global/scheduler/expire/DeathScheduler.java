package com.paymong.global.scheduler.expire;

import com.paymong.global.code.SchedulerMessage;
import com.paymong.global.code.SchedulerExpire;
import com.paymong.global.scheduler.BasicScheduler;
import com.paymong.global.task.expire.DeathTask;
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
public class DeathScheduler implements BasicSchedulerExpire {
    private static final Map<Long, SchedulerWithExpireVo> deathSchedulerMap =  new HashMap<>();
    private final DeathTask deathTask;

    @Value("${scheduler.logger}")
    private Boolean logger;

    @Override
    public void startScheduler(Long mongId) {
        if (deathSchedulerMap.containsKey(mongId)) {
            BasicScheduler.writeLog(mongId, SchedulerMessage.DEATH_ALREADY);
            return;
        }

        // 새로운 Death 스케줄러 생성
        SchedulerWithExpireVo schedulerWithExpireVo = SchedulerWithExpireVo.builder()
                .startTime(LocalDateTime.now())
                .expire(SchedulerExpire.DEATH_EXPIRE.expire)
                .message(SchedulerExpire.DEATH_EXPIRE.message)
                .runnable(getRunnable(mongId))
                .build();
        // 초기화
        schedulerWithExpireVo.init();
        // Death 스케줄러 Map 에 저장
        deathSchedulerMap.put(mongId, schedulerWithExpireVo);

        BasicScheduler.writeLogWithExpire(mongId, SchedulerMessage.DEATH_START, schedulerWithExpireVo.getExpire());
    }

    @Override
    public void pauseScheduler(Long mongId) {
        if (deathSchedulerMap.containsKey(mongId)) {
            // Death 스케줄러 일시정지
            SchedulerWithExpireVo schedulerWithExpireVo = deathSchedulerMap.get(mongId);
            Long pastTime = Duration.between(schedulerWithExpireVo.getStartTime(), LocalDateTime.now()).toSeconds();
            Long expire = schedulerWithExpireVo.getExpire() - pastTime;

            schedulerWithExpireVo.getScheduler().shutdown();
            schedulerWithExpireVo.setExpire(expire);

            BasicScheduler.writeLogWithExpire(mongId, SchedulerMessage.DEATH_PAUSE, expire);
        } else
            BasicScheduler.writeLog(mongId, SchedulerMessage.DEATH_NOT_FOUND);
    }

    @Override
    public void restartScheduler(Long mongId) {
        if (deathSchedulerMap.containsKey(mongId)) {
            // Death 스케줄러 재시작 (일시중지 스케줄러 재가동)
            SchedulerWithExpireVo schedulerWithExpireVo = deathSchedulerMap.get(mongId);
            schedulerWithExpireVo.setStartTime(LocalDateTime.now());
            schedulerWithExpireVo.init();

            BasicScheduler.writeLogWithExpire(mongId, SchedulerMessage.DEATH_RESTART, schedulerWithExpireVo.getExpire());
        } else
            BasicScheduler.writeLog(mongId, SchedulerMessage.DEATH_NOT_FOUND);
    }

    @Override
    public void stopScheduler(Long mongId) {
        if (deathSchedulerMap.containsKey(mongId)) {
            // Death 스케줄러 중지
            SchedulerWithExpireVo schedulerWithExpireVo = deathSchedulerMap.get(mongId);
            schedulerWithExpireVo.getScheduler().shutdown();
            // Death 스케줄러 Map 에서 삭제
            deathSchedulerMap.remove(mongId);

            if (logger)
                BasicScheduler.writeLog(mongId, SchedulerMessage.DEATH_STOP);
        } else
            BasicScheduler.writeLog(mongId, SchedulerMessage.DEATH_NOT_FOUND);
    }

    @Override
    public Runnable getRunnable(Long mongId) {
        return () -> {
            deathTask.afterStop(mongId, this);
        };
    }
}
