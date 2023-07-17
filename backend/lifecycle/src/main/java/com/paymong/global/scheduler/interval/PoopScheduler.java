package com.paymong.global.scheduler.interval;

import com.paymong.global.code.SchedulerInterval;
import com.paymong.global.code.SchedulerMessage;
import com.paymong.global.scheduler.BasicScheduler;
import com.paymong.global.task.interval.PoopTask;
import com.paymong.global.vo.SchedulerWithIntervalVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class PoopScheduler implements BasicSchedulerInterval {
    private static final Map<Long, SchedulerWithIntervalVo> poopSchedulerMap =  new HashMap<>();
    private final PoopTask poopTask;

    @Value("${scheduler.logger}")
    private Boolean logger;

    @Override
    public void startScheduler(Long mongId) {
        if (poopSchedulerMap.containsKey(mongId)) {
            BasicScheduler.writeLog(mongId, SchedulerMessage.POOP_ALREADY);
            return;
        }

        // 랜덤 시간 생성
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
//        long randomInterval = random.nextInt(31) + 30;
//        long poopInterval = randomInterval * 60;
        long poopInterval = 5L;

        // 새로운 Poop 스케줄러 생성
        SchedulerWithIntervalVo schedulerWithIntervalVo = SchedulerWithIntervalVo.builder()
                .message(SchedulerInterval.POOP_INTERVAL.message)
                .interval(poopInterval)
                .runnable(getRunnable(mongId))
                .build();
        // 초기화
        schedulerWithIntervalVo.init();
        // Poop 스케줄러 Map 에 저장
        poopSchedulerMap.put(mongId, schedulerWithIntervalVo);

        BasicScheduler.writeLogWithInterval(mongId, SchedulerMessage.POOP_START, schedulerWithIntervalVo.getInterval());
    }

    @Override
    public void stopScheduler(Long mongId) {
        if (poopSchedulerMap.containsKey(mongId)) {
            // Poop 스케줄러 중지
            SchedulerWithIntervalVo schedulerWithIntervalVo = poopSchedulerMap.get(mongId);
            schedulerWithIntervalVo.getScheduler().shutdown();
            // Poop 스케줄러 Map 에서 삭제
            poopSchedulerMap.remove(mongId);

            if (logger)
                BasicScheduler.writeLog(mongId, SchedulerMessage.POOP_STOP);
        } else
            BasicScheduler.writeLog(mongId, SchedulerMessage.POOP_NOT_FOUND);
    }

    @Override
    public Runnable getRunnable(Long mongId) {
        return () -> {
            poopTask.afterStop(mongId, this);
        };
    }
}
