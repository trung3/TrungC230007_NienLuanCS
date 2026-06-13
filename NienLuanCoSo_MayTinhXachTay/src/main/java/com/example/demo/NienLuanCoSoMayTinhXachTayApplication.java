package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "com.example")
@EnableJpaRepositories(basePackages = "com.example.repository")
@org.springframework.boot.persistence.autoconfigure.EntityScan(basePackages = "com.example.entity")
public class NienLuanCoSoMayTinhXachTayApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            NienLuanCoSoMayTinhXachTayApplication.class,
            args
        );
    }
}