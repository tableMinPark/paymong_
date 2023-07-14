package com.paymong.mong.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "status_code")
public class StatusCode {
    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "point")
    private Integer point;

    @Column(name = "strength")
    private Integer strength;

    @Column(name = "health")
    private Integer health;

    @Column(name = "satiety")
    private Integer satiety;

    @Column(name = "sleep")
    private Integer sleep;

    @Column(name = "weight")
    private Integer weight;
}
