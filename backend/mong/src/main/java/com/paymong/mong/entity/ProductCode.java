package com.paymong.mong.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "product_code")
public class ProductCode {
    @Id
    @Column(name = "product_code_id")
    private Long productCodeId;

    @Column(name = "code")
    private String code;

    @Column(name = "price")
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "status_code")
    private StatusCode statusCode;
}
