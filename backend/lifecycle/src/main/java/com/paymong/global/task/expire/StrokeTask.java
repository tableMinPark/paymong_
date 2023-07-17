package com.paymong.global.task.expire;

import com.paymong.global.code.TaskMessage;
import com.paymong.global.exception.fail.NotFoundFailException;
import com.paymong.global.scheduler.BasicScheduler;
import com.paymong.global.task.BasicTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StrokeTask implements BasicTask {

    @Override
    @Transactional
    public void afterStop(Long mongId, BasicScheduler basicScheduler) throws NotFoundFailException {
        basicScheduler.stopScheduler(mongId);
        BasicTask.writeLog(mongId, TaskMessage.STROKE);
    }
}
