package com.devops.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Calculator App - Entry Point
 *
 * SpringBootServletInitializer extend karna zaroori hai
 * kyunki jab WAR file Tomcat pe deploy hoti hai,
 * Tomcat ko batana padta hai ki Spring Boot app kaise start kare.
 */
@SpringBootApplication
public class CalculatorApplication extends SpringBootServletInitializer {

    // Tomcat WAR deploy karne ke liye yeh method use karta hai
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CalculatorApplication.class);
    }

    // Local run ke liye: java -jar calculator-app.war
    public static void main(String[] args) {
        SpringApplication.run(CalculatorApplication.class, args);
    }
}
