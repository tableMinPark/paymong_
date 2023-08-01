package com.paymong.mong.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "mong_history")
public class MongHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_history_id")
    private Long mongHistoryId;

    @Column(name = "mong_id")
    private Long mongId;

    @Column(name = "code")
    private String code;
}
