package com.axoniq.monolith.auctionhouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataAnalyticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataAnalyticsApplication.class, args);
    }
}
