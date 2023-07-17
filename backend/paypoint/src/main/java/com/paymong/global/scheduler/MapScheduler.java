package com.paymong.global.scheduler;

import com.paymong.global.code.SchedulerExpire;
import com.paymong.global.code.SchedulerMessage;
import com.paymong.global.task.MapTask;
import com.paymong.global.vo.SchedulerVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MapScheduler implements BasicScheduler {
    private static final Map<Long, SchedulerVo> deathSchedulerMap =  new HashMap<>();
    private final MapTask mapTask;

    @Override
    public void startScheduler(Long memberId) {
        if (deathSchedulerMap.containsKey(memberId)) {
            BasicScheduler.writeLog(memberId, SchedulerMessage.MAP_ALREADY);
            return;
        }

        // 새로운 MAP 스케줄러 생성
        SchedulerVo schedulerVo = SchedulerVo.builder()
                .startTime(LocalDateTime.now())
                .expire(SchedulerExpire.MAP_EXPIRE.expire)
                .message(SchedulerExpire.MAP_EXPIRE.message)
                .runnable(getRunnable(memberId))
                .build();
        // 초기화
        schedulerVo.init();
        // MAP 스케줄러 Map 에 저장
        deathSchedulerMap.put(memberId, schedulerVo);

        BasicScheduler.writeLogWithExpire(memberId, SchedulerMessage.MAP_START, schedulerVo.getExpire());
    }

    @Override
    public void pauseScheduler(Long memberId) {
        if (deathSchedulerMap.containsKey(memberId)) {
            // Map 스케줄러 일시정지
            SchedulerVo schedulerVo = deathSchedulerMap.get(memberId);
            Long pastTime = Duration.between(schedulerVo.getStartTime(), LocalDateTime.now()).toSeconds();
            Long expire = schedulerVo.getExpire() - pastTime;

            schedulerVo.getScheduler().shutdown();
            schedulerVo.setExpire(expire);

            BasicScheduler.writeLogWithExpire(memberId, SchedulerMessage.MAP_PAUSE, expire);
        } else
            BasicScheduler.writeLog(memberId, SchedulerMessage.MAP_NOT_FOUND);
    }

    @Override
    public void restartScheduler(Long memberId) {
        if (deathSchedulerMap.containsKey(memberId)) {
            // Map 스케줄러 재시작 (일시중지 스케줄러 재가동)
            SchedulerVo schedulerVo = deathSchedulerMap.get(memberId);
            schedulerVo.setStartTime(LocalDateTime.now());
            schedulerVo.init();

            BasicScheduler.writeLogWithExpire(memberId, SchedulerMessage.MAP_RESTART, schedulerVo.getExpire());
        } else
            BasicScheduler.writeLog(memberId, SchedulerMessage.MAP_NOT_FOUND);
    }

    @Override
    public void stopScheduler(Long memberId) {
        if (deathSchedulerMap.containsKey(memberId)) {
            // Map 스케줄러 중지
            SchedulerVo schedulerVo = deathSchedulerMap.get(memberId);
            schedulerVo.getScheduler().shutdown();
            // Map 스케줄러 Map 에서 삭제
            deathSchedulerMap.remove(memberId);

            BasicScheduler.writeLog(memberId, SchedulerMessage.MAP_STOP);
        } else
            BasicScheduler.writeLog(memberId, SchedulerMessage.MAP_NOT_FOUND);
    }

    @Override
    public Runnable getRunnable(Long memberId) {
        return () -> mapTask.afterStop(memberId, this);
    }
}
