package com.paymong.auth.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "common_code")
public class CommonCode {
    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "group_code")
    private String groupCode;

    @Column(name = "name")
    private String name;
}
