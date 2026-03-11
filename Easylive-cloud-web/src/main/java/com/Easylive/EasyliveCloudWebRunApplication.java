package com.Easylive;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.Easylive")
@EnableFeignClients
@EnableScheduling
public class EasyliveCloudWebRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyliveCloudWebRunApplication.class, args);
    }
}
