package com.Easylive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.Easylive"})
public class EasyliveCloudGatewayRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyliveCloudGatewayRunApplication.class, args);
    }
}
