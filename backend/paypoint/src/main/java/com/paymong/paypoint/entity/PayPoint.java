package com.paymong.paypoint.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "pay_point")
public class PayPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_point_id")
    private Long payPointId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "point")
    private Integer point;
}
