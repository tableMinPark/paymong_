package com.paymong.global.task.interval;

import com.paymong.global.code.LifeCycleFailCode;
import com.paymong.global.code.MongActiveCode;
import com.paymong.global.code.MongConditionCode;
import com.paymong.global.code.TaskMessage;
import com.paymong.global.exception.fail.NotFoundFailException;
import com.paymong.global.scheduler.BasicScheduler;
import com.paymong.global.task.BasicTask;
import com.paymong.lifecycle.entity.Mong;
import com.paymong.lifecycle.entity.MongHistory;
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
public class PoopTask implements BasicTask {
    private final MongRepository mongRepository;
    private final MongHistoryRepository mongHistoryRepository;

    @Override
    @Transactional
    public void afterStop(Long mongId, BasicScheduler basicScheduler) throws NotFoundFailException {
        basicScheduler.stopScheduler(mongId);

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(LifeCycleFailCode.NOT_FOUND_MONG));

        String statusCode = mong.getStatusCode();

        // ##### 똥 생성 적용하지 않을 조건 확인 #####
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
        Integer penalty = mong.getPenalty();
        // ##### 페널티 조건 확인 #####
        if (poopCount == 4) {
            // 패널티 적립
            mong.setPenalty(penalty + 1);

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
            // 패널티 로그 저장
            mongHistoryRepository.save(MongHistory.builder()
                    .mongId(mongId)
                    .code(MongActiveCode.PENALTY.code)
                    .build());
        }
        // ##### 정상 로직 (새로운 똥 생성 스케줄러 생성) #####
        else {
            // 배변 추가
            mong.setPoopCount(poopCount + 1);
            // 배변 로그 저장
            mongHistoryRepository.save(MongHistory.builder()
                    .mongId(mongId)
                    .code(MongActiveCode.POOP.code)
                    .build());
            BasicTask.writeLogWithData(mongId, TaskMessage.POOP, mong.getPoopCount());
            basicScheduler.startScheduler(mongId);
        }
    }
}
