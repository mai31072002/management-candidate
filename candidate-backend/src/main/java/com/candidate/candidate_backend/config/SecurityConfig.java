package com.candidate.candidate_backend.config;

import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.candidate.candidate_backend.exception.RestAccessDeniedHandler;
import com.candidate.candidate_backend.exception.RestAuthenticationEntryPoint;

import javax.crypto.spec.SecretKeySpec;
import java.util.Collection;

@Configuration
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {
    /**
     * Một số thuật toán mã hóa pass: (tại sao phải mã hóa pass vì nếu có bị lộ mã thì hackcer cũng khó mà ăng nhập)
     * --> Tăng tời gian để phát hiện có hacker để có thể chặn lại lỗ hổng
     * - MD5 : Hash nhanh, không salt
     * - SHA1 : Hash nhanh, ít phức tạp
     * - SHA256 : Bảo mật hơn SHA1 nhưng vẫn nhanh
     * --> Hash nhanh đồng nghĩa không an toán, dễ bị brute-force
     * --> Nhất với SHA1 đã bị phá
     *
     * - PBKDF2 : Có thể cấu hình số vòng lặp
     *
     *
     * Thuật toán Khuyên dùng hiện nay
     * BCrypt (Tính phổ biến) : An toàn, phổ biến nhất hiện nay
     * Argon2 (mới hiện nay) : Thuật toán mới, tối ưu chống GPU/ASIC
     * chống GPU/ASIC: VD hackcer có đoạn mã hóa pass thì các thuật toán trên mình dùng thì hackcer cũng dùng được và
     * hackcer dùng để thực hiện mã hóa các mật khẩu để tìm đúng mật khẩu
     * - Ví dụ:
     * CPU thử được ~100.000 mật khẩu/giây
     * GPU có thể thử hàng triệu → hàng tỷ mật khẩu/giây
     * ASIC : Chip chuyên dụng được thiết kế chỉ để làm 1 việc (ví dụ đào Bitcoin).Nếu xây ASIC để bẻ mật khẩu → tốc độ
     * còn khủng khiếp hơn GPU.
     *
     * --> Mà Argon2 chống việc này bằng cách Mỗi lần băm có thể cần 64MB, 128MB, 256MB RAM Và phải truy cập bộ nhớ
     * --> Nếu hacker muốn brute-force 1 triệu mật khẩu cùng lúc:
     * liên tụcHọ cần hàng chục TB RAM -> Chi phí cực kỳ cao
     * */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Mở tất cả quyền test API
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/**").permitAll()
//            );
//
//        return http.build();
//    }

    private final String[] PUBLIC_ENPOINT = {
            "/error",
            "/api/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
    };

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    // Cấu hình Phân quyền
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity httpSecurity,
            RestAuthenticationEntryPoint restAuthenticationEntryPoint,
            RestAccessDeniedHandler restAccessDeniedHandler
    ) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->
                request
                        .requestMatchers(PUBLIC_ENPOINT).permitAll()
//                        .requestMatchers(HttpMethod.PUT, "/api/auth/refresh-token").permitAll()
//                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
        );

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(
                        jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
        );

        httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HmacSHA256");

        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    // Converter scope (SCOPE_ADMIN) sang role (ROLE_ADMIN)
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        // Converter lấy role
        JwtGrantedAuthoritiesConverter roleConverter = new JwtGrantedAuthoritiesConverter();
//        roleConverter.setAuthoritiesClaimName("scope");
        roleConverter.setAuthorityPrefix("ROLE_");

        // Converter lấy permission
        JwtGrantedAuthoritiesConverter permissionConverter = new JwtGrantedAuthoritiesConverter();
        permissionConverter.setAuthoritiesClaimName("permission");
        permissionConverter.setAuthorityPrefix("");

        // Converter tổng hợp
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = roleConverter.convert(jwt);
            authorities.addAll(permissionConverter.convert(jwt));
            return authorities;
        });

        return converter;
    }
}
