package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackageClasses = OutboxDemoApplication.class)
@EnableJpaRepositories(basePackageClasses = OutboxDemoApplication.class)
public class OutboxDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutboxDemoApplication.class, args);
    }


}
