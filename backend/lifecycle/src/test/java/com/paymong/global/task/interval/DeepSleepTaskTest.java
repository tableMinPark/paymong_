package com.paymong.global.task.interval;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeepSleepTaskTest {

    private static final Long DAY_SECONDS = 86400L;

    @Test
    void getSecondsTest() {
        // interval : 수면 시간 (초)
        LocalTime sleepStart = LocalTime.of(0, 0, 0);
        LocalTime sleepEnd = LocalTime.of(0, 0, 5);
        long seconds = Duration.between(sleepStart, sleepEnd).getSeconds();
        long interval = seconds < 0 ? DAY_SECONDS - Math.abs(seconds) : seconds;
        assertEquals(interval, 5L);

        sleepStart = LocalTime.of(0, 0, 5);
        sleepEnd = LocalTime.of(0, 0, 0);
        seconds = Duration.between(sleepStart, sleepEnd).getSeconds();
        interval = seconds < 0 ? DAY_SECONDS - Math.abs(seconds) : seconds;
        assertEquals(interval, 86395L);

        sleepStart = LocalTime.of(23, 59, 55);
        sleepEnd = LocalTime.of(0, 0, 10);
        seconds = Duration.between(sleepStart, sleepEnd).getSeconds();
        interval = seconds < 0 ? DAY_SECONDS - Math.abs(seconds) : seconds;
        assertEquals(interval, 15L);

        sleepStart = LocalTime.of(0, 0, 0);
        sleepEnd = LocalTime.of(23, 59, 55);
        seconds = Duration.between(sleepStart, sleepEnd).getSeconds();
        interval = seconds < 0 ? DAY_SECONDS - Math.abs(seconds) : seconds;
        assertEquals(interval, 86395L);
    }

    @Test
    void getDateTest_1() {
        LocalDateTime now = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        LocalTime nowTime = now.toLocalTime();
        LocalTime sleepStart = LocalTime.of(0, 0, 5);
        LocalTime sleepEnd = LocalTime.of(0, 0, 15);

        LocalDate start;
        LocalDate end;

        if (nowTime.isBefore(sleepStart))
            start = now.toLocalDate();
        else
            start = now.toLocalDate().plusDays(1);

        if (nowTime.isBefore(sleepEnd))
            end = now.toLocalDate();
        else
            end = now.toLocalDate().plusDays(1);
        assertEquals(start, LocalDate.of(2023, 1, 1));
        assertEquals(end, LocalDate.of(2023, 1, 1));
    }

    @Test
    void getDateTest_2() {
        LocalDateTime now = LocalDateTime.of(2023, 1, 1, 0, 0, 10);
        LocalTime nowTime = now.toLocalTime();
        LocalTime sleepStart = LocalTime.of(0, 0, 5);
        LocalTime sleepEnd = LocalTime.of(0, 0, 15);

        LocalDate start;
        LocalDate end;

        if (nowTime.isBefore(sleepStart))
            start = now.toLocalDate();
        else
            start = now.toLocalDate().plusDays(1);

        if (nowTime.isBefore(sleepEnd))
            end = now.toLocalDate();
        else
            end = now.toLocalDate().plusDays(1);
        assertEquals(start, LocalDate.of(2023, 1, 1));
        assertEquals(end, LocalDate.of(2023, 1, 1));
    }

    @Test
    void getDateTest_3() {
        LocalDateTime now = LocalDateTime.of(2023, 1, 1, 0, 0, 20);
        LocalTime nowTime = now.toLocalTime();
        LocalTime sleepStart = LocalTime.of(0, 0, 5);
        LocalTime sleepEnd = LocalTime.of(0, 0, 15);

        LocalDate start;
        LocalDate end;

        if (nowTime.isBefore(sleepStart))
            start = now.toLocalDate();
        else
            start = now.toLocalDate().plusDays(1);

        if (nowTime.isBefore(sleepEnd))
            end = now.toLocalDate();
        else
            end = now.toLocalDate().plusDays(1);
        assertEquals(start, LocalDate.of(2023, 1, 2));
        assertEquals(end, LocalDate.of(2023, 1, 2));
    }
}