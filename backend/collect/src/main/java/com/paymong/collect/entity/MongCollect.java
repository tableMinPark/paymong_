package com.paymong.collect.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "mong_collect")
public class MongCollect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_collect_id")
    private Long mongId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "mong_code")
    private String mongCode;
}
