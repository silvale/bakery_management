package com.bakery.bakery_management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*");

//        registry.addMapping("/api/**") // Cho phép tất cả các đường dẫn bắt đầu bằng /api
//                .allowedOrigins("http://localhost:3000") // Thay bằng URL của FE (ví dụ React là 3000)
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowCredentials(true);
    }
}