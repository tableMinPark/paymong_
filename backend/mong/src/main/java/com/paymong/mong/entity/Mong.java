package com.paymong.mong.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.ws.rs.DefaultValue;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "mong")
public class Mong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_id")
    private Long mongId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "mong_code")
    private String mongCode;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "map_code")
    private String mapCode;

    @Column(name = "name")
    private String name;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "age")
    private Integer age;

    @Column(name = "strength")
    private Integer strength;

    @Column(name = "satiety")
    private Integer satiety;

    @Column(name = "health")
    private Integer health;

    @Column(name = "sleep")
    private Integer sleep;

    @Column(name = "penalty")
    private Integer penalty;

    @Column(name = "training_count")
    private Integer trainingCount;

    @Column(name = "stroke_count")
    private Integer strokeCount;

    @Column(name = "poop_count")
    private Integer poopCount;

    @Column(name = "sleep_start")
    private LocalTime sleepStart;

    @Column(name = "sleep_end")
    private LocalTime sleepEnd;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "reg_dt")
    @CreationTimestamp
    private LocalDateTime regDt;

    public static Mong of(Long memberId, String mongCode, String name, LocalTime sleepStart, LocalTime sleepEnd) {
        return Mong.builder()
                .memberId(memberId)
                .mongCode(mongCode)
                .statusCode("CD000")
                .mapCode("MP000")
                .name(name)
                .weight(0)
                .age(0)
                .strength(0)
                .satiety(0)
                .health(0)
                .sleep(0)
                .penalty(0)
                .trainingCount(0)
                .strokeCount(0)
                .poopCount(0)
                .sleepStart(sleepStart)
                .sleepEnd(sleepEnd)
                .active(true)
                .build();
    }
}
