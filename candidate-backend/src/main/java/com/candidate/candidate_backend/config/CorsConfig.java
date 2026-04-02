package com.candidate.candidate_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.List;

@Configuration
public class CorsConfig {

    // ==================================
    // Cấu hình domain FE gọi API BE
    // ==================================
    @Bean
        CorsFilter corsFilter(@Value("${app.cors.allowedOrigins:*}") List<String> allowedOrigins) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // cho phép request gửi kèm: cookie, Authorization header, Session
        config.setAllowCredentials(true);
        config.setAllowedOrigins(allowedOrigins);
         // Cho phép tất cả HTTP Method
        config.setAllowedMethods(Collections.singletonList("*"));
        // Cho phép tất cả Header
        config.setAllowedHeaders(Collections.singletonList("*"));
        //  Áp dụng cho toàn bộ API
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
