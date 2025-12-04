package com.yachaerang.yachaerangbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.yachaerang.yachaerangbatch")
@EnableScheduling
public class YachaerangBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(YachaerangBatchApplication.class, args);
    }

}
