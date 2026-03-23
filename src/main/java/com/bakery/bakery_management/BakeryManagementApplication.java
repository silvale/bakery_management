package com.bakery.bakery_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BakeryManagementApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BakeryManagementApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
