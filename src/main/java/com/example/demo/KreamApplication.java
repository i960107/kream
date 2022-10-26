package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KreamApplication {
    private static Logger logger = LoggerFactory.getLogger(KreamApplication.class);
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:application-aws.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(KreamApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}
