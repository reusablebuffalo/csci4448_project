package com.friendlyreminder.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Driver class that runs SpringApplication, which handles Dependency Injection and configuration defined in pom.xml and application.properties
 */
@SpringBootApplication
public class Application {

    /**
     * Top-level driving function for the application, allows Spring to initialize application based on application properties and pom.xml
     * @param args SpringApplication start-up arguments, {@link SpringBootApplication} annotation links application properties
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
