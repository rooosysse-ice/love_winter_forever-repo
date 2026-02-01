package com.Easylive.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.Easylive"}, exclude = {DataSourceAutoConfiguration.class})
public class EasyliveAdminRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyliveAdminRunApplication.class, args);
    }
}
