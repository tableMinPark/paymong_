package com.paymong.global.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchedulerWithIntervalVo {
    protected ThreadPoolTaskScheduler scheduler;
    protected Runnable runnable;
    protected String message;

    private Long interval;

    public void init(){
        this.scheduler = new ThreadPoolTaskScheduler();
        this.scheduler.initialize();
        this.scheduler.setThreadNamePrefix(this.message);
        this.scheduler.scheduleWithFixedDelay(runnable, Date.from(Instant.now().plusSeconds(interval)), interval * 1000L);
    }
}
