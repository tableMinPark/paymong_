package com.paymong.global.task.interval;

import com.paymong.global.code.LifeCycleFailCode;
import com.paymong.global.code.MongConditionCode;
import com.paymong.global.code.TaskMessage;
import com.paymong.global.exception.fail.NotFoundFailException;
import com.paymong.global.scheduler.BasicScheduler;
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
public class SatietyTask implements BasicTask {
    private final MongRepository mongRepository;
    private final MongHistoryRepository mongHistoryRepository;

    private final DeathScheduler deathScheduler;

    @Override
    @Transactional
    public void afterStop(Long mongId, BasicScheduler basicScheduler) throws NotFoundFailException {

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(LifeCycleFailCode.NOT_FOUND_MONG));

        String statusCode = mong.getStatusCode();

        // ##### 포만감 적용 받지 않을 조건 확인 #####
        List<String> filters = Arrays.asList(
                MongConditionCode.SLEEP.code,
                MongConditionCode.DIE.code,
                MongConditionCode.GRADUATE.code
        );
        if (filters.contains(statusCode)) {
            basicScheduler.startScheduler(mongId);
            BasicTask.writeLog(mongId, TaskMessage.INVALID_POOP);
            return;
        }

        Integer poopCount = mong.getPoopCount();
        Integer satiety = mong.getSatiety();

        // ##### 배변 카운트로 포만감 감소 조건 확인 #####
        if (poopCount == 0)
            satiety = Math.max(0, satiety - 1);
        else
            satiety = Math.max(0, satiety - (poopCount * poopCount));

        // ##### 포만감 감소량 갱신 #####
        mong.setSatiety(satiety);
        BasicTask.writeLogWithData(mongId, TaskMessage.SATIETY, satiety);

        // ##### 포만감이 5 미만인 경우 조건 확인 #####
        if (satiety < 5) {
            filters = Arrays.asList(
                    MongConditionCode.NORMAL.code,
                    MongConditionCode.HUNGRY.code
            );
            // 배고픔 상태로 돌입
            if (filters.contains(statusCode)) {
                mong.setStatusCode(MongConditionCode.HUNGRY.code);
                BasicTask.writeLog(mongId, TaskMessage.HUNGRY);
            }

            if (satiety == 0) {
                BasicTask.writeLog(mongId, TaskMessage.HUNGRY);
                basicScheduler.stopScheduler(mongId);
                // 배고픔이 바닥이라 죽음 스케줄러 실행 (죽음 카운터 시작)
                deathScheduler.startScheduler(mongId);
            }
        }

        BasicTask.writeLogWithData(mongId, TaskMessage.SATIETY, mong.getSatiety());
    }
}
