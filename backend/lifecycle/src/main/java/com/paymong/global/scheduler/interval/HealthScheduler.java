package com.paymong.global.scheduler.interval;

import com.paymong.global.code.SchedulerInterval;
import com.paymong.global.code.SchedulerMessage;
import com.paymong.global.scheduler.BasicScheduler;
import com.paymong.global.task.interval.HealthTask;
import com.paymong.global.vo.SchedulerWithIntervalVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HealthScheduler implements BasicSchedulerInterval {
    private static final Map<Long, SchedulerWithIntervalVo> healthSchedulerMap =  new HashMap<>();
    private final HealthTask healthTask;

    @Value("${scheduler.logger}")
    private Boolean logger;

    @Override
    public void startScheduler(Long mongId) {
        if (healthSchedulerMap.containsKey(mongId)) {
            BasicScheduler.writeLog(mongId, SchedulerMessage.HEALTH_ALREADY);
            return;
        }
        // 새로운 Health 스케줄러 생성
        SchedulerWithIntervalVo schedulerWithIntervalVo = SchedulerWithIntervalVo.builder()
                .message(SchedulerInterval.HEALTH_INTERVAL.message)
                .interval(SchedulerInterval.HEALTH_INTERVAL.interval)
                .runnable(getRunnable(mongId))
                .build();
        // 초기화
        schedulerWithIntervalVo.init();
        // Health 스케줄러 Map 에 저장
        healthSchedulerMap.put(mongId, schedulerWithIntervalVo);

        BasicScheduler.writeLogWithInterval(mongId, SchedulerMessage.HEALTH_START, schedulerWithIntervalVo.getInterval());
    }

    @Override
    public void stopScheduler(Long mongId) {
        if (healthSchedulerMap.containsKey(mongId)) {
            // Health 스케줄러 중지
            SchedulerWithIntervalVo schedulerWithIntervalVo = healthSchedulerMap.get(mongId);
            schedulerWithIntervalVo.getScheduler().shutdown();
            // Health 스케줄러 Map 에서 삭제
            healthSchedulerMap.remove(mongId);

            if (logger)
                BasicScheduler.writeLog(mongId, SchedulerMessage.HEALTH_STOP);
        } else
            BasicScheduler.writeLog(mongId, SchedulerMessage.HEALTH_NOT_FOUND);
    }

    @Override
    public Runnable getRunnable(Long mongId) {
        return () -> {
            healthTask.afterStop(mongId, this);
        };
    }
}
