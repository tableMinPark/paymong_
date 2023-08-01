package com.paymong.collect.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "map_collect")
public class MapCollect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_collect_id")
    private Long mapCollectId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "map_code")
    private String mapCode;
}
