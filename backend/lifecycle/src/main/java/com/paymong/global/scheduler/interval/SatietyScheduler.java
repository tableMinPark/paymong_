package com.paymong.global.scheduler.interval;

import com.paymong.global.code.SchedulerInterval;
import com.paymong.global.code.SchedulerMessage;
import com.paymong.global.scheduler.BasicScheduler;
import com.paymong.global.task.interval.SatietyTask;
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
public class SatietyScheduler implements BasicSchedulerInterval {
    private static final Map<Long, SchedulerWithIntervalVo> satietySchedulerMap =  new HashMap<>();
    private final SatietyTask satietyTask;

    @Value("${scheduler.logger}")
    private Boolean logger;

    @Override
    public void startScheduler(Long mongId) {
        if (satietySchedulerMap.containsKey(mongId)) {
            BasicScheduler.writeLog(mongId, SchedulerMessage.SATIETY_ALREADY);
            return;
        }

        // 새로운 Satiety 스케줄러 생성
        SchedulerWithIntervalVo schedulerWithIntervalVo = SchedulerWithIntervalVo.builder()
                .message(SchedulerInterval.SATIETY_INTERVAL.message)
                .interval(SchedulerInterval.SATIETY_INTERVAL.interval)
                .runnable(getRunnable(mongId))
                .build();
        // 초기화
        schedulerWithIntervalVo.init();
        // Satiety 스케줄러 Map 에 저장
        satietySchedulerMap.put(mongId, schedulerWithIntervalVo);

        BasicScheduler.writeLogWithInterval(mongId, SchedulerMessage.SATIETY_START, schedulerWithIntervalVo.getInterval());
    }

    @Override
    public void stopScheduler(Long mongId) {
        if (satietySchedulerMap.containsKey(mongId)) {
            // Satiety 스케줄러 중지
            SchedulerWithIntervalVo schedulerWithIntervalVo = satietySchedulerMap.get(mongId);
            schedulerWithIntervalVo.getScheduler().shutdown();
            // Satiety 스케줄러 Map 에서 삭제
            satietySchedulerMap.remove(mongId);

            if (logger)
                BasicScheduler.writeLog(mongId, SchedulerMessage.SATIETY_STOP);
        } else
            BasicScheduler.writeLog(mongId, SchedulerMessage.SATIETY_NOT_FOUND);
    }

    @Override
    public Runnable getRunnable(Long mongId) {
        return () -> {
            satietyTask.afterStop(mongId, this);
        };
    }
}
