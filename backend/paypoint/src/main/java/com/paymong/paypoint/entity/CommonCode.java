package com.paymong.paypoint.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column(name = "group_name")
    private String groupName;
}
