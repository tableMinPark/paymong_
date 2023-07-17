package com.paymong.global.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchedulerWithExpireVo {
    protected ThreadPoolTaskScheduler scheduler;
    protected Runnable runnable;
    protected String message;

    private LocalDateTime startTime;
    private Long expire;

    public void init(){
        this.scheduler = new ThreadPoolTaskScheduler();
        this.scheduler.initialize();
        this.scheduler.setThreadNamePrefix(this.message);
        this.scheduler.schedule(this.runnable, Instant.now().plusSeconds(this.expire));
    }
}
