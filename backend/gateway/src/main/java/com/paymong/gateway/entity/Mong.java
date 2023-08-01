package com.paymong.gateway.entity;

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

    @Column(name = "active")
    private Boolean active;
}
