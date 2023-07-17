package com.paymong.global.task.expire;

import com.paymong.global.exception.fail.NotFoundFailException;
import com.paymong.global.scheduler.*;
import com.paymong.global.scheduler.expire.DeathScheduler;
import com.paymong.global.scheduler.expire.EvolutionScheduler;
import com.paymong.global.scheduler.expire.StrokeScheduler;
import com.paymong.global.scheduler.interval.HealthScheduler;
import com.paymong.global.scheduler.interval.PoopScheduler;
import com.paymong.global.scheduler.interval.SatietyScheduler;
import com.paymong.global.task.BasicTask;
import com.paymong.lifecycle.repository.MongHistoryRepository;
import com.paymong.lifecycle.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SleepTask implements BasicTask {
    private final MongRepository mongRepository;
    private final MongHistoryRepository mongHistoryRepository;

    private DeathScheduler deathScheduler;
    private EvolutionScheduler evolutionScheduler;
    private HealthScheduler healthScheduler;
    private PoopScheduler poopScheduler;
    private SatietyScheduler satietyScheduler;
    private StrokeScheduler strokeScheduler;

    @Override
    @Transactional
    public void afterStop(Long mongId, BasicScheduler basicScheduler) throws NotFoundFailException {
        basicScheduler.stopScheduler(mongId);


    }
}
