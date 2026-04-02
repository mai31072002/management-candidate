package com.candidate.candidate_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Thêm config cho Annotation @EnableJpaAuditing để thêm tự động datetime cho createAt
 * - Tại sao không thêm trong main -> cũng chỉ để Spring Boot quét đc thôi đâu phải tạo fileConfig này
 * ---> Mục đích tạo file riêng để sau hỗ trợ cho mở rộng nếu setting Sau này
 * */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
