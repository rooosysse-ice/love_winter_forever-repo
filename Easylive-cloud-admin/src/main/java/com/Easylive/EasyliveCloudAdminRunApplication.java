package com.Easylive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.Easylive"})
@EnableFeignClients
public class EasyliveCloudAdminRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyliveCloudAdminRunApplication.class, args);
    }
}
