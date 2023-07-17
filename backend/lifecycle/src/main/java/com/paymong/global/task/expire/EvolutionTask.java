package com.paymong.global.task.expire;

import com.paymong.global.code.LifeCycleFailCode;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class EvolutionTask implements BasicTask {
    private final MongRepository mongRepository;
    private final MongHistoryRepository mongHistoryRepository;

    @Override
    @Transactional
    public void afterStop(Long mongId, BasicScheduler basicScheduler) throws NotFoundFailException {
        // 일시적인 스케줄러 이기 때문에 중지
        basicScheduler.stopScheduler(mongId);

        Mong mong = mongRepository.findById(mongId)
                .orElseThrow(() -> new NotFoundFailException(LifeCycleFailCode.NOT_FOUND_MONG));

        mong.setStatusCode(MongConditionCode.EVOLUTION_READY.code);

        // 진화 로그 저장
        mongHistoryRepository.save(MongHistory.builder()
                .mongId(mongId)
                .code(MongConditionCode.EVOLUTION_READY.code)
                .build());

        BasicTask.writeLog(mongId, TaskMessage.EVOLUTION);
    }
}
