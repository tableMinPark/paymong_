package com.paymong.global.task;

import com.paymong.global.code.TaskMessage;
import com.paymong.global.scheduler.BasicScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface BasicTask {
    void afterStop(Long mongId, BasicScheduler basicScheduler);

    Logger log = LoggerFactory.getLogger(BasicScheduler.class);
    static void writeLog(Long mongId, TaskMessage code) {
        String scheduleLog = String.format("%5d : %-30s", mongId, code.message);
        log.info(scheduleLog);
    }

    static void writeLogWithData(Long mongId, TaskMessage code, Integer data) {
        String scheduleLog = String.format("%5d : %-31s : [잔여정보 : %7d]", mongId, code.message, data);
        log.info(scheduleLog);
    }
}
