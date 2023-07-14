package com.paymong.pay_point.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "pay_point_history")
public class PayPointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_point_history_id")
    private Long payPointHistoryId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "point")
    private Integer point;

    @Column(name = "code")
    private String code;

    @Column(name = "comment")
    private String comment;

    @Column(name = "reg_dt")
    @CreationTimestamp
    private LocalDateTime regDt;
}
