package com.paymong.global.scheduler.expire;

import com.paymong.global.scheduler.BasicScheduler;

public interface BasicSchedulerExpire extends BasicScheduler {
    void pauseScheduler(Long mongId);
    void restartScheduler(Long mongId);
}
