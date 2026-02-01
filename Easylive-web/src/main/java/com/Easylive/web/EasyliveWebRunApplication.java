package com.Easylive.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.Easylive"}, exclude = {DataSourceAutoConfiguration.class})
public class EasyliveWebRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyliveWebRunApplication.class, args);
    }
}
