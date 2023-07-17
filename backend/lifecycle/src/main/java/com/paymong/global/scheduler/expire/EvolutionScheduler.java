package com.paymong.global.scheduler.expire;

import com.paymong.global.code.SchedulerExpire;
import com.paymong.global.code.SchedulerMessage;
import com.paymong.global.scheduler.BasicScheduler;
import com.paymong.global.task.expire.EvolutionTask;
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
public class EvolutionScheduler implements BasicSchedulerExpire {
    private static final Map<Long, SchedulerWithExpireVo> evolutionSchedulerMap =  new HashMap<>();
    private final EvolutionTask evolutionTask;

    @Value("${scheduler.logger}")
    private Boolean logger;

    @Override
    public void startScheduler(Long mongId) {
        if (evolutionSchedulerMap.containsKey(mongId)) {
            BasicScheduler.writeLog(mongId, SchedulerMessage.EVOLUTION_ALREADY);
            return;
        }

        // 새로운 Evolution 스케줄러 생성
        SchedulerWithExpireVo schedulerWithExpireVo = SchedulerWithExpireVo.builder()
                .startTime(LocalDateTime.now())
                .expire(SchedulerExpire.EVOLUTION_EXPIRE_LV1.expire)
                .message(SchedulerExpire.EVOLUTION_EXPIRE_LV1.message)
                .runnable(getRunnable(mongId))
                .build();
        // 초기화
        schedulerWithExpireVo.init();
        // Evolution 스케줄러 Map 에 저장
        evolutionSchedulerMap.put(mongId, schedulerWithExpireVo);

        BasicScheduler.writeLogWithExpire(mongId, SchedulerMessage.EVOLUTION_START, schedulerWithExpireVo.getExpire());
    }

    @Override
    public void pauseScheduler(Long mongId) {
        if (evolutionSchedulerMap.containsKey(mongId)) {
            // Evolution 스케줄러 일시정지
            SchedulerWithExpireVo schedulerWithExpireVo = evolutionSchedulerMap.get(mongId);
            Long pastTime = Duration.between(schedulerWithExpireVo.getStartTime(), LocalDateTime.now()).toSeconds();
            Long expire = schedulerWithExpireVo.getExpire() - pastTime;

            schedulerWithExpireVo.getScheduler().shutdown();
            schedulerWithExpireVo.setExpire(expire);

            BasicScheduler.writeLogWithExpire(mongId, SchedulerMessage.EVOLUTION_PAUSE, expire);
        } else
            BasicScheduler.writeLog(mongId, SchedulerMessage.EVOLUTION_NOT_FOUND);
    }

    @Override
    public void restartScheduler(Long mongId) {
        if (evolutionSchedulerMap.containsKey(mongId)) {
            // Evolution 스케줄러 재시작 (일시중지 스케줄러 재가동)
            SchedulerWithExpireVo schedulerWithExpireVo = evolutionSchedulerMap.get(mongId);
            schedulerWithExpireVo.setStartTime(LocalDateTime.now());
            schedulerWithExpireVo.init();

            BasicScheduler.writeLogWithExpire(mongId, SchedulerMessage.EVOLUTION_RESTART, schedulerWithExpireVo.getExpire());
        } else
            BasicScheduler.writeLog(mongId, SchedulerMessage.EVOLUTION_NOT_FOUND);
    }

    @Override
    public void stopScheduler(Long mongId) {
        if (evolutionSchedulerMap.containsKey(mongId)) {
            // Evolution 스케줄러 중지
            SchedulerWithExpireVo schedulerWithExpireVo = evolutionSchedulerMap.get(mongId);
            schedulerWithExpireVo.getScheduler().shutdown();
            // Evolution 스케줄러 Map 에서 삭제
            evolutionSchedulerMap.remove(mongId);

            if (logger)
                BasicScheduler.writeLog(mongId, SchedulerMessage.EVOLUTION_STOP);
        } else
            BasicScheduler.writeLog(mongId, SchedulerMessage.EVOLUTION_NOT_FOUND);
    }

    public void nextLevelScheduler(Long mongId, Integer level) {
        if (evolutionSchedulerMap.containsKey(mongId)) {
            // 강제 진화 시 다음 레벨로 가기 위한 기존 진화 대기 스케줄러 중단
            evolutionSchedulerMap.get(mongId).getScheduler().shutdown();
            BasicScheduler.writeLog(mongId, SchedulerMessage.EVOLUTION_NEXT_LEVEL_DELETE);
        } else {
            // 진화 대기 시간이 지나서 스케줄러에 없는 경우 스케줄러 객체 생성해서 저장 (아래에서 시간 수정)
            SchedulerWithExpireVo schedulerWithExpireVo = new SchedulerWithExpireVo();
            schedulerWithExpireVo.setRunnable(getRunnable(mongId));
            evolutionSchedulerMap.put(mongId, schedulerWithExpireVo);
            BasicScheduler.writeLog(mongId, SchedulerMessage.EVOLUTION_NEXT_LEVEL_REGISTER);
        }

        SchedulerWithExpireVo schedulerWithExpireVo = evolutionSchedulerMap.get(mongId);

        switch (level) {
            case 1:
                schedulerWithExpireVo.setExpire(SchedulerExpire.EVOLUTION_EXPIRE_LV1.expire);
                schedulerWithExpireVo.setMessage(SchedulerExpire.EVOLUTION_EXPIRE_LV1.message);
                break;
            case 2:
                schedulerWithExpireVo.setExpire(SchedulerExpire.EVOLUTION_EXPIRE_LV2.expire);
                schedulerWithExpireVo.setMessage(SchedulerExpire.EVOLUTION_EXPIRE_LV2.message);
                break;
            case 3:
                schedulerWithExpireVo.setExpire(SchedulerExpire.EVOLUTION_EXPIRE_LV3.expire);
                schedulerWithExpireVo.setMessage(SchedulerExpire.EVOLUTION_EXPIRE_LV3.message);
                break;
            default:
                BasicScheduler.writeLog(mongId, SchedulerMessage.EVOLUTION_LEVEL);
        }

        schedulerWithExpireVo.setStartTime(LocalDateTime.now());
        schedulerWithExpireVo.init();

        BasicScheduler.writeLogWithExpire(mongId, SchedulerMessage.EVOLUTION_NEXT_LEVEL, schedulerWithExpireVo.getExpire());
    }

    @Override
    public Runnable getRunnable(Long mongId) {
        return () -> {
            evolutionTask.afterStop(mongId, this);
        };
    }
}
