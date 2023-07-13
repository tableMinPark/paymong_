package com.paymong.mong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MongApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongApplication.class, args);
    }

}
