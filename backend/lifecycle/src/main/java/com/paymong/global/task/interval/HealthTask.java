package com.paymong.global.task.interval;

import com.paymong.global.code.LifeCycleFailCode;
import com.paymong.global.code.MongConditionCode;
import com.paymong.global.code.TaskMessage;
import com.paymong.global.exception.fail.NotFoundFailException;
import com.paymong.global.scheduler.BasicScheduler;
import com.paymong.global.scheduler.interval.BasicSchedulerInterval;
import com.paymong.global.scheduler.expire.DeathScheduler;
import com.paymong.global.task.BasicTask;
import com.paymong.lifecycle.entity.Mong;
import com.paymong.lifecycle.repository.MongHistoryRepository;
import com.paymong.lifecycle.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HealthTask implements BasicTask {
    private final MongRepository mongRepository;
    private final MongHistoryRepository mongHistoryRepository;

    private final DeathScheduler deathScheduler;

    @Override
    @Transactional
    public void afterStop(Long mongId, BasicScheduler basicScheduler) throws NotFoundFailException {
        Mong mong = mongRepository.findById(mongId)
                .orElseGet(() -> {
                    BasicSchedulerInterval healthScheduler = (BasicSchedulerInterval) basicScheduler;
                    healthScheduler.stopScheduler(mongId);
                    throw new NotFoundFailException(LifeCycleFailCode.NOT_FOUND_MONG);
                });

        String statusCode = mong.getStatusCode();

        // ##### 체력 감소 불가 조건 확인 #####
        List<String> filters = Arrays.asList(
                MongConditionCode.SLEEP.code,
                MongConditionCode.DIE.code,
                MongConditionCode.GRADUATE.code
        );
        if (filters.contains(statusCode)) {
            BasicTask.writeLog(mongId, TaskMessage.INVALID_HEALTH);
            return;
        }

        Integer poopCount = mong.getPoopCount();
        Integer health = mong.getHealth();

        // ##### 체력 감소량 결정 #####
        if (poopCount == 0)
            health = Math.max(0, health - 1);
        else
            health = Math.max(0, health - (poopCount * poopCount));

        // ##### 체력 감소량 갱신 #####
        mong.setHealth(health);
        BasicTask.writeLogWithData(mongId, TaskMessage.HEALTH, health);

        // ##### 체력이 바닥났을 조건 확인 #####
        if (health == 0) {
            filters = Arrays.asList(
                    MongConditionCode.NORMAL.code,
                    MongConditionCode.SOMNOLENCE.code,
                    MongConditionCode.HUNGRY.code
            );
            // 아픈 상태로 돌입
            if (filters.contains(statusCode)) {
                mong.setStatusCode(MongConditionCode.SICK.code);
                BasicTask.writeLog(mongId, TaskMessage.SICK);
            }
            // 기존 체력감소 스케줄러 중지
            basicScheduler.stopScheduler(mongId);
            // 체력이 바닥이라 죽음 스케줄러 실행 (죽음 카운터 시작)
            deathScheduler.startScheduler(mongId);
        }
    }
}
