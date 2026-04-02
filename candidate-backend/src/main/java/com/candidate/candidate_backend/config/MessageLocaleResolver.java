package com.candidate.candidate_backend.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class MessageLocaleResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {
    List<Locale> localeCodes = Arrays.asList(new Locale("vi"), new Locale("en"));

    /**
    * @author datpv
    * @date 2021-05-18 21:39:09
    * @param request
    * @return
    */
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
    String headerLang = request.getHeader("Accept-Language");
    return headerLang == null || headerLang.isEmpty()
        ? Locale.getDefault()
        : Locale.lookup(Locale.LanguageRange.parse(headerLang), localeCodes);
    }

    /**
    * @author datpv
    * @date 2021-05-18 21:30:26
    * @return
    */
    @Bean
    public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
    rs.setBasename("messages");
    rs.setDefaultEncoding("UTF-8");

    // Nếu không tìm thấy key trong properties sẽ trả lại chính key đó thay vì throw exception
    rs.setUseCodeAsDefaultMessage(true);
    return rs;
    }
}
