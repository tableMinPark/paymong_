package com.paymong.pay_point;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PayPointApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayPointApplication.class, args);
    }

}
