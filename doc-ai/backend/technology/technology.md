* Core Stack

    Language: Java 17  (Sử dụng Record, Sealed Classes, Pattern Matching).

    Framework: Spring Boot 3.2.5.

    Build Tool: Gradle.

    Database: PostgreSQL 18.

# Architecture & Conventions

    Layering: Controller -> Service -> Repository -> Entity.

    DTO Mapping: Sử dụng MapStruct để chuyển đổi giữa Entity và DTO (Hiện trong code đang dùng mapper bằng tay).

    Validation: Sử dụng spring-boot-starter-validation (Bean Validation).

    lombok: lombok

    Bảo mật: Spring security + JWT

    Mã hóa Pass : BCrypt in Spring Security

    JDBC: JPA / Hibernate

    Exception Handling: Sử dụng @RestControllerAdvice và Custom exception
