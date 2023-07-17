package com.paymong.global.scheduler;

import com.paymong.global.code.SchedulerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface BasicScheduler {
    void startScheduler(Long mongId);
    void stopScheduler(Long mongId);
    void pauseScheduler(Long mongId);
    void restartScheduler(Long mongId);
    Runnable getRunnable(Long mongId);

    Logger log = LoggerFactory.getLogger(BasicScheduler.class);
    static void writeLog(Long mongId, SchedulerMessage code) {
        String scheduleLog = String.format("%5d : %-30s", mongId, code.message);
        log.info(scheduleLog);
    }

    static void writeLogWithExpire(Long mongId, SchedulerMessage code, Long expire) {
        String scheduleLog = String.format("%5d : %-30s : [남은시간 : %6ds]", mongId, code.message, expire);
        log.info(scheduleLog);
    }
}
